import React, { useState, useEffect } from 'react';
import { apiClient } from '../api/client';
import { Plus, Trash2, Copy, Check } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export const Students = () => {
  const { user } = useAuth();
  const [students, setStudents] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  
  // Form State
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    birthDate: '',
    address: ''
  });
  
  const [newStudentCreds, setNewStudentCreds] = useState<any>(null);
  const [copied, setCopied] = useState(false);

  const fetchStudents = async () => {
    try {
      setLoading(true);
      const data: any = await apiClient.get('/students');
      setStudents(data);
    } catch (error) {
      console.error('Failed to fetch students', error);
      alert('Failed to load students');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (user?.role === 'KINDERGARTEN_ADMIN') {
      fetchStudents();
    }
  }, [user]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const data = await apiClient.post('/students/register', formData);
      setNewStudentCreds(data);
      setFormData({ firstName: '', lastName: '', birthDate: '', address: '' });
      fetchStudents();
    } catch (error: any) {
       console.error('Failed to create student', error);
       alert(error.response?.data?.message || 'Failed to create student');
    }
  };

  const handleCopy = () => {
    if (newStudentCreds) {
      navigator.clipboard.writeText(`Username: ${newStudentCreds.username}\nPassword: ${newStudentCreds.plainPassword}`);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    }
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setNewStudentCreds(null);
  };

  const handleDelete = async (id: string) => {
    if (window.confirm('Haqiqatdan ham ushbu o\'quvchini o\'chirmoqchimisiz?')) {
      try {
        await apiClient.delete(`/students/${id}`);
        fetchStudents();
      } catch (error: any) {
        console.error('Failed to delete student', error);
        alert('Failed to delete student');
      }
    }
  };

  if (loading) return <div className="flex items-center justify-center p-12"><div className="text-gray-500">Loading students...</div></div>;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">O'quvchilar</h1>
          <p className="text-sm text-gray-500 mt-1">Jami o'quvchilar soni: <span className="font-semibold text-indigo-600">{students.length} ta</span></p>
        </div>
        <button
          onClick={() => setIsModalOpen(true)}
          className="flex items-center px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors"
        >
          <Plus className="h-5 w-5 mr-2" />
          Yangi qo'shish
        </button>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-100 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">№</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">O'quvchi F.I.O</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tug'ilgan sana</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Manzil</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Username</th>
                <th scope="col" className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">Amallar</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {students.map((student: any, index: number) => (
                <tr key={student.id} className="hover:bg-gray-50 transition-colors">
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {index + 1}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <div className="flex-shrink-0 h-8 w-8 rounded-full bg-indigo-100 flex items-center justify-center">
                        <span className="font-medium text-indigo-700 text-xs">
                          {student.firstName?.[0] || ''}{student.lastName?.[0] || ''}
                        </span>
                      </div>
                      <div className="ml-3">
                        <div className="text-sm font-medium text-gray-900">{student.firstName} {student.lastName}</div>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {student.birthDate ? new Date(student.birthDate).toLocaleDateString('ru-RU') : '-'}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    <div className="truncate max-w-xs" title={student.address || '-'}>
                      {student.address || '-'}
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-indigo-50 text-indigo-700">
                      {student.username}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-center text-sm font-medium">
                    <button 
                      onClick={() => handleDelete(student.userId)} 
                      className="text-red-500 hover:text-red-700 bg-red-50 hover:bg-red-100 p-1.5 rounded transition-colors border border-red-100 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 inline-flex items-center justify-center"
                      title="O'chirish"
                    >
                      <Trash2 className="h-4 w-4" />
                    </button>
                  </td>
                </tr>
              ))}
              {students.length === 0 && (
                <tr>
                  <td colSpan={6} className="px-6 py-8 text-center text-gray-500 text-sm">
                    Bu bog'chada o'quvchilar yo'q. "Yangi qo'shish" tugmasini bosib o'quvchi qo'shishingiz mumkin.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modal */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-gray-900 bg-opacity-75 flex items-center justify-center z-50 p-4 sm:p-0">
          <div className="bg-white rounded-xl w-full max-w-md p-6 sm:p-8 shadow-2xl relative">
            
            {!newStudentCreds ? (
              <>
                <h2 className="text-2xl font-bold text-gray-900 mb-6">Yangi O'quvchi Qo'shish</h2>
                <form onSubmit={handleSubmit} className="space-y-5">
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">Ismi</label>
                      <input
                        required
                        name="firstName"
                        type="text"
                        value={formData.firstName}
                        onChange={handleInputChange}
                        placeholder="Aziz"
                        className="block w-full rounded-md border-gray-300 shadow-sm p-2.5 border focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">Familiyasi</label>
                      <input
                        required
                        name="lastName"
                        type="text"
                        value={formData.lastName}
                        onChange={handleInputChange}
                        placeholder="Karimov"
                        className="block w-full rounded-md border-gray-300 shadow-sm p-2.5 border focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                      />
                    </div>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Tug'ilgan sanasi</label>
                    <input
                      required
                      name="birthDate"
                      type="date"
                      value={formData.birthDate}
                      onChange={handleInputChange}
                      className="block w-full rounded-md border-gray-300 shadow-sm p-2.5 border focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Yashash manzili</label>
                    <input
                      required
                      name="address"
                      type="text"
                      value={formData.address}
                      onChange={handleInputChange}
                      placeholder="Toshkent sh., Yunusobod t."
                      className="block w-full rounded-md border-gray-300 shadow-sm p-2.5 border focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    />
                  </div>

                  <div className="flex justify-end space-x-3 mt-8 pt-4 border-t border-gray-100">
                    <button
                      type="button"
                      onClick={closeModal}
                      className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                    >
                      Bekor qilish
                    </button>
                    <button
                      type="submit"
                      className="px-4 py-2 text-sm font-medium text-white bg-indigo-600 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                    >
                      Saqlash va Login yaratish
                    </button>
                  </div>
                </form>
              </>
            ) : (
              // Success View for Credentials
              <div className="text-center py-4">
                <div className="mx-auto flex items-center justify-center h-16 w-16 rounded-full bg-green-100 mb-6">
                  <Check className="h-8 w-8 text-green-600" />
                </div>
                <h3 className="text-2xl font-bold text-gray-900 mb-2">O'quvchi muvaffaqiyatli qo'shildi!</h3>
                <p className="text-sm text-gray-600 mb-6">
                  Iltimos, o'quvchi uchun yaratilgan login ma'lumotlarni saqlab oling.
                  <br/><span className="font-bold text-red-600 mt-1 inline-block">Diqqat: Parol faqat bir marta ko'rsatiladi!</span>
                </p>
                <div className="bg-gray-50 rounded-lg p-5 text-left border border-gray-200 mb-8 shadow-inner">
                  <div className="flex items-center justify-between mb-3 pb-3 border-b border-gray-200">
                    <span className="text-sm font-medium text-gray-500 uppercase tracking-wider">Username</span>
                    <span className="font-mono font-bold text-gray-900 text-lg">{newStudentCreds.username}</span>
                  </div>
                  <div className="flex items-center justify-between">
                    <span className="text-sm font-medium text-gray-500 uppercase tracking-wider">Password</span>
                    <span className="font-mono font-bold text-indigo-600 text-lg">{newStudentCreds.plainPassword}</span>
                  </div>
                </div>
                
                <div className="flex space-x-4 justify-center">
                  <button
                    onClick={handleCopy}
                    className="flex-1 flex justify-center items-center px-4 py-3 border-2 border-indigo-600 text-indigo-600 text-sm font-bold rounded-lg hover:bg-indigo-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors"
                  >
                    {copied ? <Check className="h-5 w-5 mr-2"/> : <Copy className="h-5 w-5 mr-2"/> }
                    {copied ? 'Nusxa olindi!' : 'Nusxa olish'}
                  </button>
                  <button
                    onClick={closeModal}
                    className="flex-1 flex justify-center items-center px-4 py-3 bg-indigo-600 text-white text-sm font-bold rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors"
                  >
                    Yopish
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
};
