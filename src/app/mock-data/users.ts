// mock-data/users.ts
export interface User {
  // Datos de registro (obligatorios)
  name: string;
  lastname: string;
  email: string;
  password: string;

  // Datos de membresía (opcionales)
  plan?: string;
  validUntil?: string;
  memberId?: string;
}
export type AuthenticatedUser = Omit<User, 'password'>;

  export const USERS: User[] = [
    {
      name: 'Administrador',
      lastname: 'Gym',
      email: 'admin@smartgym.com',
      password: '123456',
      plan: 'Premium',
      validUntil: '12/25',
      memberId: '****1111'
    },
    {
      name: 'Andre',
      lastname: 'Alvites',
      email: 'andre@smartgym.com',
      password: '1234567',
      plan: 'Básico',
      validUntil: '10/25',
      memberId: '****2222'
    }
  ];
