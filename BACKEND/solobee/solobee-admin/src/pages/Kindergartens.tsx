import React, { useState, useEffect } from 'react';
import { apiClient } from '../api/client';
import { Plus, Trash2 } from 'lucide-react';

export const Kindergartens = () => {
  const [kindergartens, setKindergartens] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    address: '',
    adminUsername: '',
    adminPassword: '',
  });

  const fetchKindergartens = async () => {
    try {
      setLoading(true);
      const data: any = await apiClient.get('/kindergartens');
      setKindergartens(data);
    } catch (error) {
      console.error('Failed to fetch kindergartens', error);
      alert('Failed to load kindergartens');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchKindergartens();
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await apiClient.post('/kindergartens', formData);
      setIsModalOpen(false);
      setFormData({
        name: '',
        address: '',
        adminUsername: '',
        adminPassword: '',
      });
      fetchKindergartens();
    } catch (error: any) {
      console.error('Failed to create kindergarten', error);
      alert(error.response?.data?.message || 'Failed to create kindergarten');
    }
  };

  const handleDelete = async (id: string, name: string) => {
    const isConfirmed = window.confirm(`Are you sure you want to delete "${name}"? This will also permanently delete all its admins and students.`);
    if (!isConfirmed) return;

    try {
      await apiClient.delete(`/kindergartens/${id}`);
      fetchKindergartens();
    } catch (error: any) {
      console.error('Failed to delete kindergarten', error);
      alert(error.response?.data?.message || 'Failed to delete kindergarten');
    }
  };

  if (loading) return <div>Loading kindergartens...</div>;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900">Kindergartens</h1>
        <button
          onClick={() => setIsModalOpen(true)}
          className="flex items-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
        >
          <Plus className="h-5 w-5 mr-2" />
          Add Kindergarten
        </button>
      </div>

      <div className="bg-white rounded-lg shadow overflow-hidden border border-gray-100">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Name
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Address
              </th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Actions
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {kindergartens.map((kg: any) => (
              <tr key={kg.id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                  {kg.name}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {kg.address || 'N/A'}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <button
                    onClick={() => handleDelete(kg.id, kg.name)}
                    className="text-red-600 hover:text-red-900 focus:outline-none p-1 rounded-md hover:bg-red-50"
                    title="Delete Kindergarten"
                  >
                    <Trash2 className="h-5 w-5" />
                  </button>
                </td>
              </tr>
            ))}
            {kindergartens.length === 0 && (
              <tr>
                <td colSpan={3} className="px-6 py-4 text-center text-gray-500 text-sm">
                  No kindergartens found.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Modal */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg w-full max-w-md p-6 shadow-xl">
            <h2 className="text-xl font-bold mb-4">Add New Kindergarten</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Name</label>
                <input
                  required
                  name="name"
                  type="text"
                  value={formData.name}
                  onChange={handleChange}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border focus:ring-blue-500 focus:border-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Address (Optional)</label>
                <input
                  name="address"
                  type="text"
                  value={formData.address}
                  onChange={handleChange}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border focus:ring-blue-500 focus:border-blue-500"
                />
              </div>

              <div className="border-t pt-4 mt-4">
                <h3 className="text-lg font-medium mb-3">Admin Account Details</h3>
                <div className="space-y-3">
                  <div>
                    <label className="block text-sm font-medium text-gray-700">Admin Username</label>
                    <input
                      required
                      name="adminUsername"
                      type="text"
                      value={formData.adminUsername}
                      onChange={handleChange}
                      className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border focus:ring-blue-500 focus:border-blue-500"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700">Admin Password (min 6 chars)</label>
                    <input
                      required
                      name="adminPassword"
                      type="password"
                      minLength={6}
                      value={formData.adminPassword}
                      onChange={handleChange}
                      className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border focus:ring-blue-500 focus:border-blue-500"
                    />
                  </div>
                </div>
              </div>

              <div className="flex justify-end space-x-3 mt-6">
                <button
                  type="button"
                  onClick={() => setIsModalOpen(false)}
                  className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700"
                >
                  Create
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
