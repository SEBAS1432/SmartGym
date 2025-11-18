import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-chatbot-widget',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './chatbot-widget.component.html',
  styleUrls: ['./chatbot-widget.component.scss']
})
export class ChatbotWidgetComponent {

  
  isChatOpen = false;

  mensajes: any[] = [];

  preguntasFrecuentes = [
    {
      pregunta: '¿Cuáles son los horarios del gimnasio?',
      respuesta: 'Nuestras instalaciones están abiertas 24/7 para todos los miembros activos.'
    },
    {
      pregunta: '¿Cómo puedo cancelar una clase?',
      respuesta: 'Puedes cancelar tus clases desde la sección "Mis Reservas". Ten en cuenta que solo puedes cancelar hasta 2 horas antes de que comience la clase.'
    },
    {
      pregunta: '¿Dónde veo mi progreso?',
      respuesta: 'Puedes ver tu historial de peso y otros datos en la sección "Mi Progreso" de tu dashboard.'
    },
    {
      pregunta: '¿Cómo contacto a un entrenador?',
      respuesta: 'Puedes ver la lista de entrenadores en la sección "Clases" o acercarte al mostrador principal para agendar una sesión personalizada.'
    }
  ];

  ngOnInit() {
    this.mensajes.push({ 
      autor: 'bot', 
      texto: '¡Hola! Soy tu asistente virtual. ¿En qué puedo ayudarte hoy?' 
    });
  }

  toggleChat(): void {
    this.isChatOpen = !this.isChatOpen;
    if (this.isChatOpen) {
      this.mensajes = [{ 
        autor: 'bot', 
        texto: '¡Hola! Soy tu asistente virtual. Aquí tienes algunas preguntas frecuentes:' 
      }];
    }
  }

  seleccionarPregunta(pregunta: any): void {
    this.mensajes.push({ autor: 'usuario', texto: pregunta.pregunta });

    setTimeout(() => {
      this.mensajes.push({ autor: 'bot', texto: pregunta.respuesta });
    }, 500); // 500ms de espera
  }
}