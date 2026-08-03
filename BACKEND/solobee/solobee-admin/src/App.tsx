import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { DashboardLayout } from './layouts/DashboardLayout';
import { Login } from './pages/Login';
import { Dashboard } from './pages/Dashboard';
import { Kindergartens } from './pages/Kindergartens';
import { Courses } from './pages/Courses';
import { CategoryDetail } from './pages/CategoryDetail';
import { SubCategoryDetail } from './pages/SubCategoryDetail';
import { TopicDetail } from './pages/TopicDetail';
import { AvatarManagement } from './pages/AvatarManagement';
import { Students } from './pages/Students';

const GuestRoute = ({ children }: { children: React.ReactNode }) => {
  const { user, isLoading } = useAuth();

  if (isLoading) {
    return <div className="min-h-screen flex items-center justify-center">Loading...</div>;
  }

  if (user) {
    return <Navigate to="/" replace />;
  }

  return <>{children}</>;
};

export const App = () => {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route
            path="/login"
            element={
              <GuestRoute>
                <Login />
              </GuestRoute>
            }
          />

          <Route element={<DashboardLayout />}>
            <Route path="/" element={<Dashboard />} />
            <Route path="/kindergartens" element={<Kindergartens />} />
            <Route path="/courses" element={<Courses />} />
            <Route path="/courses/:categoryId" element={<CategoryDetail />} />
            <Route path="/courses/:categoryId/:subCategoryId" element={<SubCategoryDetail />} />
            <Route path="/courses/:categoryId/:subCategoryId/:topicId" element={<TopicDetail />} />
            <Route path="/avatars" element={<AvatarManagement />} />
            <Route path="/students" element={<Students />} />
          </Route>

          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
};

export default App;
