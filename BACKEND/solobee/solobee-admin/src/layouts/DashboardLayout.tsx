import { Navigate, Outlet, Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { LogOut, Home, BookOpen, Users, Smile, GraduationCap } from 'lucide-react';

export const DashboardLayout = () => {
  const { user, logout, isLoading } = useAuth();
  const location = useLocation();

  if (isLoading) {
    return <div className="min-h-screen flex items-center justify-center">Loading...</div>;
  }

  if (!user) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  if (user.role !== 'SUPER_ADMIN' && user.role !== 'ADMIN') {
    return <Navigate to="/login" replace />;
  }

  const navLinks = [
    { name: 'Dashboard', icon: Home, path: '/' },
    { name: 'Kindergartens', icon: Users, path: '/kindergartens' },
    { name: 'Courses', icon: BookOpen, path: '/courses' },
    { name: 'Students', icon: GraduationCap, path: '/students' },
    { name: 'Avatars', icon: Smile, path: '/avatars' },
  ];
  const handleLogout = () => {
    logout();
  };

  return (
    <div className="flex h-screen bg-gray-50">
      <aside className="w-64 bg-white border-r border-gray-200">
        <div className="h-16 flex items-center px-6 font-bold text-xl text-primary-600 border-b">
          Savodxon Admin
        </div>

        <nav className="p-4 space-y-1">
          {navLinks.map((item) => {
            const Icon = item.icon;
            const isActive =
              item.path === '/'
                ? location.pathname === '/'
                : location.pathname.startsWith(item.path);
            return (
              <Link
                key={item.name}
                to={item.path}
                className={`flex items-center px-4 py-3 rounded-lg text-sm font-medium ${
                  isActive
                    ? 'bg-primary-50 text-primary-700'
                    : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'
                }`}
              >
                <Icon className="mr-3 h-5 w-5" />
                {item.name}
              </Link>
            )
          })}
        </nav>
      </aside>

      <main className="flex-1 flex flex-col overflow-hidden">
        <header className="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-6">
          <div className="text-gray-600">
            Welcome, <span className="font-semibold text-gray-900">{user.fullName || user.username}</span>
            <span className="text-sm ml-2 px-2 py-0.5 rounded-full bg-blue-100 text-blue-800">
              {user.role}
            </span>
          </div>
          <button
            onClick={handleLogout}
            className="flex items-center text-gray-600 hover:text-red-600 transition-colors"
          >
            <LogOut className="h-5 w-5 mr-2" />
            Logout
          </button>
        </header>

        <div className="flex-1 overflow-auto p-6">
          <Outlet />
        </div>
      </main>
    </div>
  );
};
