import React from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { LogOut, Menu, User, ChevronDown } from 'lucide-react';

const Navbar = ({ title, icon: Icon }) => {
  const navigate = useNavigate();
  const [isMenuOpen, setIsMenuOpen] = React.useState(false);
  
  // Get user data from localStorage
  const userString = localStorage.getItem('user');
  const user = userString ? JSON.parse(userString) : null;
  
  const handleLogout = () => {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    navigate('/');
  };

  return (
    <nav className="sticky top-0 z-50 px-6 py-4 flex justify-between items-center md:px-16 lg:px-24 border-b border-white/10 backdrop-blur-sm bg-white/5">
      <div className="flex items-center space-x-3">
        {Icon && <Icon size={24} className="text-emerald-400" />}
        <h1 className="text-2xl font-bold tracking-tight text-white">
          {title}
        </h1>
      </div>

      <div className="relative">
        <motion.button
          whileHover={{ scale: 1.02 }}
          whileTap={{ scale: 0.98 }}
          onClick={() => setIsMenuOpen(!isMenuOpen)}
          className="flex items-center space-x-2 px-4 py-2 rounded-lg bg-white/5 hover:bg-white/10 border border-white/10"
        >
          {user?.imageUrl ? (
            <img 
              src={user.imageUrl} 
              alt={user.name} 
              className="w-8 h-8 rounded-full"
            />
          ) : (
            <User size={20} className="text-white" />
          )}
          <span className="text-white text-sm hidden md:block">{user?.name || 'User'}</span>
          <ChevronDown size={16} className="text-white" />
        </motion.button>

        {isMenuOpen && (
          <motion.div
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            className="absolute right-0 mt-2 w-48 rounded-lg bg-slate-800 border border-white/10 shadow-lg py-1"
          >
            <button
              onClick={handleLogout}
              className="flex items-center w-full px-4 py-2 text-sm text-white hover:bg-white/5"
            >
              <LogOut size={16} className="mr-2" />
              Sign Out
            </button>
          </motion.div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
