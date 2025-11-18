export type Rol = 'ADMIN' | 'TRAINER' | 'CLIENTE';
export type EstadoUsuario = 'ACTIVO' | 'INACTIVO';

export interface UsuarioRequest {
  nombres: string;
  apellidos: string;
  correo: string;
  contrasena: string;
  telefono?: string | null;
  rol?: Rol | null;
  estado?: EstadoUsuario | null;
}

export interface UsuarioResponse {
  id: number;
  nombres: string;
  apellidos: string;
  correo: string;
  telefono: string | null;
  rol: Rol;
  estado: EstadoUsuario;
  fechaCreacion: string;
  fechaActualizacion: string;
}
