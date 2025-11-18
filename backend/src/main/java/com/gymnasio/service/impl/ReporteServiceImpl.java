package com.gymnasio.service.impl;

import com.gymnasio.domain.model.Pago;
import com.gymnasio.domain.model.Usuario;
import com.gymnasio.dto.ReporteSemanalResponse;
import com.gymnasio.repository.ClaseRepository;
import com.gymnasio.repository.PagoRepository;
import com.gymnasio.repository.ReservaRepository;
import com.gymnasio.repository.UsuarioRepository;
import com.gymnasio.service.ReporteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

@Service
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {

  private static final ZoneId ZONA_LIMA = ZoneId.of("America/Lima");
  private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private final UsuarioRepository usuarioRepo;
  private final PagoRepository pagoRepo;
  private final ClaseRepository claseRepo;
  private final ReservaRepository reservaRepo;

  public ReporteServiceImpl(UsuarioRepository usuarioRepo, PagoRepository pagoRepo,
                            ClaseRepository claseRepo, ReservaRepository reservaRepo) {
    this.usuarioRepo = usuarioRepo;
    this.pagoRepo = pagoRepo;
    this.claseRepo = claseRepo;
    this.reservaRepo = reservaRepo;
  }

  @Override
  public ReporteSemanalResponse obtenerReporteSemanal() {
    // Rango últimos 7 días en Lima: [inicio, fin)
    OffsetDateTime fin = LocalDate.now(ZONA_LIMA).plusDays(1).atStartOfDay(ZONA_LIMA).toOffsetDateTime();
    OffsetDateTime inicio = fin.minusDays(7);

    long nuevosUsuarios = usuarioRepo.countByFechaCreacionBetween(inicio, fin);

    long totalPagos = pagoRepo.countByStatusAndFechaPagoBetween(Pago.EstadoPago.COMPLETADO, inicio, fin);
    BigDecimal ingresosTotales =
        pagoRepo.sumMontoByStatusAndFechaPagoBetween(Pago.EstadoPago.COMPLETADO, inicio, fin);

    long clasesCreadas = claseRepo.countByFechaCreacionBetween(inicio, fin);
    long reservasHechas = reservaRepo.countByFechaReservaBetween(inicio, fin);

    // Para el DTO mostramos fechas (día) en Lima
    LocalDate li = inicio.atZoneSameInstant(ZONA_LIMA).toLocalDate();
    LocalDate lf = fin.minusNanos(1).atZoneSameInstant(ZONA_LIMA).toLocalDate();

    return new ReporteSemanalResponse(
        li,
        lf,
        nuevosUsuarios,
        totalPagos,
        (ingresosTotales != null) ? ingresosTotales : BigDecimal.ZERO,
        clasesCreadas,
        reservasHechas
    );
  }

  @Override
  public String generarReporteSemanalCsv() {
    OffsetDateTime fin = LocalDate.now(ZONA_LIMA).plusDays(1).atStartOfDay(ZONA_LIMA).toOffsetDateTime();
    OffsetDateTime inicio = fin.minusDays(7);

    StringBuilder csv = new StringBuilder();
    csv.append("Tipo,ID,Fecha,Nombre,Correo,Monto,Metodo\n");

    List<Usuario> nuevosUsuarios = usuarioRepo.findByFechaCreacionBetween(inicio, fin);
    for (Usuario u : nuevosUsuarios) {
      csv.append("NUEVO USUARIO,")
         .append(u.getId()).append(",")
         .append(formatearLima(u.getFechaCreacion())).append(",")
         .append(u.getNombres()).append(" ").append(u.getApellidos()).append(",")
         .append(u.getCorreo()).append(",,")
         .append("\n");
    }

    List<Pago> pagos = pagoRepo.findByStatusAndFechaPagoBetween(Pago.EstadoPago.COMPLETADO, inicio, fin);
    for (Pago p : pagos) {
      csv.append("PAGO COMPLETADO,")
         .append(p.getId()).append(",")
         .append(formatearLima(p.getFechaPago())).append(",")
         .append(p.getUsuario().getNombres()).append(" ").append(p.getUsuario().getApellidos()).append(",")
         .append(p.getUsuario().getCorreo()).append(",")
         .append(p.getMonto()).append(",")
         .append(p.getMetodoPago().getNombre())
         .append("\n");
    }

    return csv.toString();
  }

  @Override
  public byte[] generarReporteSemanalPdf() {
    OffsetDateTime fin = LocalDate.now(ZONA_LIMA).plusDays(1).atStartOfDay(ZONA_LIMA).toOffsetDateTime();
    OffsetDateTime inicio = fin.minusDays(7);

    ReporteSemanalResponse stats = this.obtenerReporteSemanal();
    List<Usuario> nuevosUsuarios = usuarioRepo.findByFechaCreacionBetween(inicio, fin);
    List<Pago> pagos = pagoRepo.findByStatusAndFechaPagoBetween(Pago.EstadoPago.COMPLETADO, inicio, fin);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PdfWriter writer = new PdfWriter(baos);
    PdfDocument pdf = new PdfDocument(writer);
    Document document = new Document(pdf);

    try {
      document.add(new Paragraph("Reporte Semanal SmartGym")
          .setTextAlignment(TextAlignment.CENTER)
          .setBold()
          .setFontSize(20)
          .setFontColor(ColorConstants.DARK_GRAY));

      document.add(new Paragraph("Período: " + stats.fechaInicio().format(FMT_FECHA) +
          " al " + stats.fechaFin().format(FMT_FECHA))
          .setTextAlignment(TextAlignment.CENTER)
          .setFontSize(12)
          .setMarginBottom(20));

      document.add(new Paragraph("Resumen General").setBold().setFontSize(14));
      Table kpiTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1, 1}))
          .setWidth(UnitValue.createPercentValue(100))
          .setMarginTop(10);

      kpiTable.addHeaderCell(new Cell().add(new Paragraph("Ingresos Totales")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
      kpiTable.addHeaderCell(new Cell().add(new Paragraph("Pagos Registrados")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
      kpiTable.addHeaderCell(new Cell().add(new Paragraph("Nuevos Usuarios")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
      kpiTable.addHeaderCell(new Cell().add(new Paragraph("Clases Creadas")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
      kpiTable.addHeaderCell(new Cell().add(new Paragraph("Reservas Hechas")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

      kpiTable.addCell(new Cell().add(new Paragraph("S/ " + stats.ingresosTotales())));
      kpiTable.addCell(new Cell().add(new Paragraph(String.valueOf(stats.totalPagos()))));
      kpiTable.addCell(new Cell().add(new Paragraph(String.valueOf(stats.nuevosUsuarios()))));
      kpiTable.addCell(new Cell().add(new Paragraph(String.valueOf(stats.clasesCreadas()))));
      kpiTable.addCell(new Cell().add(new Paragraph(String.valueOf(stats.reservasHechas()))));
      document.add(kpiTable);

      document.add(new Paragraph("Detalle de Nuevos Usuarios").setBold().setFontSize(14).setMarginTop(20));
      Table usuariosTable = new Table(UnitValue.createPercentArray(new float[]{1, 3, 4, 3}))
          .setWidth(UnitValue.createPercentValue(100))
          .setMarginTop(10);

      usuariosTable.addHeaderCell(new Cell().add(new Paragraph("ID")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
      usuariosTable.addHeaderCell(new Cell().add(new Paragraph("Nombre")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
      usuariosTable.addHeaderCell(new Cell().add(new Paragraph("Correo")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
      usuariosTable.addHeaderCell(new Cell().add(new Paragraph("Fecha Creación")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

      for (Usuario u : nuevosUsuarios) {
        usuariosTable.addCell(new Cell().add(new Paragraph(u.getId().toString())));
        usuariosTable.addCell(new Cell().add(new Paragraph(u.getNombres() + " " + u.getApellidos())));
        usuariosTable.addCell(new Cell().add(new Paragraph(u.getCorreo())));
        usuariosTable.addCell(new Cell().add(new Paragraph(formatearLima(u.getFechaCreacion()))));
      }
      document.add(usuariosTable);

      document.add(new Paragraph("Detalle de Pagos Completados").setBold().setFontSize(14).setMarginTop(20));
      Table pagosTable = new Table(UnitValue.createPercentArray(new float[]{2, 1, 2, 2}))
          .setWidth(UnitValue.createPercentValue(100))
          .setMarginTop(10);

      pagosTable.addHeaderCell(new Cell().add(new Paragraph("Fecha Pago")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
      pagosTable.addHeaderCell(new Cell().add(new Paragraph("Monto")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
      pagosTable.addHeaderCell(new Cell().add(new Paragraph("Cliente")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
      pagosTable.addHeaderCell(new Cell().add(new Paragraph("Método")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

      for (Pago p : pagos) {
        pagosTable.addCell(new Cell().add(new Paragraph(formatearLima(p.getFechaPago()))));
        pagosTable.addCell(new Cell().add(new Paragraph("S/ " + p.getMonto())));
        pagosTable.addCell(new Cell().add(new Paragraph(p.getUsuario().getNombres())));
        pagosTable.addCell(new Cell().add(new Paragraph(p.getMetodoPago().getNombre())));
      }
      document.add(pagosTable);

      document.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return baos.toByteArray();
  }

  /** Formatea un OffsetDateTime en hora de Lima. */
  private static String formatearLima(OffsetDateTime odt) {
    return odt.atZoneSameInstant(ZONA_LIMA).toLocalDateTime().format(FMT);
  }
}
