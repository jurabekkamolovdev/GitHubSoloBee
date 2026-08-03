import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { apiClient } from '../api/client';
import { Users } from 'lucide-react';

export const Dashboard = () => {
  const { user } = useAuth();
  const [studentCount, setStudentCount] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setLoading(true);
        // We reuse the existing /students endpoint to calculate length
        // In a real prod app, you might want a distinct /stats endpoint
        const data: any = await apiClient.get('/students');
        setStudentCount(data.length || 0);
      } catch (error) {
        console.error('Failed to fetch dashboard stats', error);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);
  
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-gray-900">Asosiy panel</h1>
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {/* O'quvchilar Soni */}
        <div className="bg-white rounded-xl shadow-sm p-6 border border-gray-100 hover:shadow-md transition-shadow relative overflow-hidden">
          <div className="absolute -right-6 -top-6 text-indigo-50 opacity-50 pointer-events-none">
            <Users className="w-32 h-32" />
          </div>
          <div className="flex items-center space-x-4 relative z-10">
            <div className="p-3 bg-indigo-100 text-indigo-600 rounded-xl">
              <Users className="w-8 h-8" />
            </div>
            <div>
              <p className="text-sm font-medium text-gray-500 uppercase tracking-wide">
                Barcha O'quvchilar
              </p>
              <h2 className="text-3xl font-bold text-gray-900 mt-1">
                {loading ? '...' : studentCount}
              </h2>
            </div>
          </div>
        </div>

      </div>
      
      <div className="bg-gradient-to-br from-indigo-50 to-white rounded-xl shadow-sm p-8 border border-indigo-100 mt-6 relative overflow-hidden">
        <div className="relative z-10">
          <h2 className="text-xl font-bold text-indigo-900 mb-2">Hurmatli {user?.fullName || user?.username}, Tizimga Xush Kelibsiz!</h2>
          <p className="text-indigo-700 max-w-2xl text-lg">
            Sizning shaxsiy "Kindergarten Admin" panelingiz orqali o'quvchilarni va ta'lim jarayonlarini osonlik bilan boshqarishingiz mumkin. Yana yangi imkoniyatlar tez kunda qo'shilib boriladi.
          </p>
          <div className="mt-6">
            <a href="/students" className="px-6 py-3 bg-indigo-600 text-white font-medium rounded-lg hover:bg-indigo-700 shadow flex inline-flex items-center transition-colors">
              <Users className="w-5 h-5 mr-2" /> O'quvchilarni Boshqarish
            </a>
          </div>
        </div>
      </div>
    </div>
  );
};
