import { useAuth } from '../context/AuthContext';

export const Dashboard = () => {
  const { user } = useAuth();
  
  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-900">Dashboard Overview</h1>
      
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white rounded-lg shadow p-6 border border-gray-100">
          <h3 className="text-sm font-medium text-gray-500 uppercase tracking-wide">
            Your Role
          </h3>
          <p className="mt-2 text-3xl font-bold text-gray-900">
            {user?.role}
          </p>
        </div>
        
        <div className="bg-white rounded-lg shadow p-6 border border-gray-100">
          <h3 className="text-sm font-medium text-gray-500 uppercase tracking-wide">
            Assigned Kindergarten
          </h3>
          <p className="mt-2 text-3xl font-bold text-gray-900">
            {user?.kindergartenId ? 'Linked' : 'Global Access'}
          </p>
        </div>
      </div>
      
      <div className="bg-white rounded-lg shadow p-8 border border-gray-100 mt-6 text-center text-gray-500">
        <p>This panel will grow with charts and management forms for Kindergartens, Courses and Students depending on the access level.</p>
      </div>
    </div>
  );
};
