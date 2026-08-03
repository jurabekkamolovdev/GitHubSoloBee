import { apiClient } from './client';

export interface StudentListItem {
  id: string;
  username: string;
  firstName: string;
  lastName: string;
  age: number;
}

export interface StudentListResponse {
  items: StudentListItem[];
  total: number;
}

export interface CreateStudentPayload {
  firstName: string;
  lastName: string;
  userName: string;
  password: string;
  age: number;
  avatarId: string;
}

export const studentsApi = {
  getStudents: (offset: number, limit: number) =>
    apiClient.get<never, StudentListResponse>('/students', {
      params: { offset, limit },
    }),

  createStudent: (data: CreateStudentPayload) =>
    apiClient.post<never, boolean>('/students/register', data),

  deleteStudent: (id: string) =>
    apiClient.delete<never, boolean>(`/students/${id}`),
};