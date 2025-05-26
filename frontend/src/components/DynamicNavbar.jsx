import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
    DollarSign,
    Bell,
    Settings,
    User,
    Users,
    MapPin,
    LogOut,
    ChevronDown,
    Home,
    PieChart
} from 'lucide-react';
import axios from 'axios';

const DynamicNavbar = () => {
    const navigate = useNavigate();
    const [userProfile, setUserProfile] = useState(null);
    const [familyProfile, setFamilyProfile] = useState(null);
    const [notifications, setNotifications] = useState([]);
    const [showProfileMenu, setShowProfileMenu] = useState(false);
    const [showNotifications, setShowNotifications] = useState(false);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        console.log('DynamicNavbar mounted, fetching data...');
        fetchUserAndFamilyData();
        fetchNotifications();
    }, []);

    const fetchUserAndFamilyData = async () => {
        try {
            console.log('Starting to fetch user data...');
            setLoading(true);
            setError(null);

            // Fetch current user info (includes Google OAuth data)
            const userResponse = await axios.get('http://localhost:8080/api/auth/user', {
                withCredentials: true,
                timeout: 10000 // 10 second timeout
            });

            console.log('User response:', userResponse.data);

            if (userResponse.data && userResponse.data.isAuthenticated) {
                setUserProfile(userResponse.data.user);
                console.log('User profile set:', userResponse.data.user);

                // Fetch family profile data
                try {
                    console.log('Fetching family profile...');
                    const familyResponse = await axios.get('http://localhost:8080/api/familyProfile', {
                        withCredentials: true,
                        timeout: 10000
                    });

                    console.log('Family response:', familyResponse.data);

                    if (familyResponse.data && familyResponse.data.length > 0) {
                        setFamilyProfile(familyResponse.data[0]);
                        console.log('Family profile set:', familyResponse.data[0]);
                    } else {
                        console.log('No family profile found');
                    }
                } catch (familyError) {
                    console.log('Family profile fetch error (this is okay):', familyError.message);
                    // This is okay - user might not have created family profile yet
                }
            } else {
                console.log('User not authenticated, redirecting...');
                navigate('/');
                return;
            }
        } catch (error) {
            console.error('Error fetching user/family data:', error);
            setError(error.message);

            if (error.response?.status === 401) {
                console.log('Unauthorized, redirecting to home...');
                navigate('/');
            } else if (error.code === 'ECONNABORTED') {
                setError('Request timeout - please check if the server is running');
            } else if (error.code === 'ERR_NETWORK') {
                setError('Network error - please check if the backend server is running on port 8080');
            }
        } finally {
            setLoading(false);
            console.log('User data fetch completed');
        }
    };

    const fetchNotifications = async () => {
        try {
            console.log('Fetching notifications...');
            const response = await axios.get('http://localhost:8080/api/notifications', {
                withCredentials: true,
                timeout: 5000
            });
            setNotifications(response.data || []);
            console.log('Notifications set:', response.data);
        } catch (error) {
            console.log('Notifications fetch failed (using mock data):', error.message);
            // Set mock notifications if API fails
            setNotifications([
                { id: 1, title: 'Welcome!', message: 'Complete your family profile', type: 'INFO', isRead: false },
                { id: 2, title: 'Investment Alert', message: 'New investment opportunity available', type: 'SUCCESS', isRead: false }
            ]);
        }
    };

    const handleLogout = async () => {
        try {
            await axios.post('http://localhost:8080/api/auth/logout', {}, {
                withCredentials: true
            });
            navigate('/');
        } catch (error) {
            console.error('Logout error:', error);
            // Force redirect even if logout API fails
            window.location.href = '/';
        }
    };

    const markNotificationAsRead = async (notificationId) => {
        try {
            await axios.patch(`http://localhost:8080/api/notifications/${notificationId}/read`, {}, {
                withCredentials: true
            });
            setNotifications(prev =>
                prev.map(notif =>
                    notif.id === notificationId ? { ...notif, isRead: true } : notif
                )
            );
        } catch (error) {
            console.error('Error marking notification as read:', error);
            // Update locally even if API fails
            setNotifications(prev =>
                prev.map(notif =>
                    notif.id === notificationId ? { ...notif, isRead: true } : notif
                )
            );
        }
    };

    const getNotificationIcon = (type) => {
        switch (type) {
            case 'WARNING': return '⚠️';
            case 'SUCCESS': return '✅';
            case 'INFO': return 'ℹ️';
            default: return '📢';
        }
    };

    // Helper function to get full name
    const getFullName = (user) => {
        if (!user) return 'User';
        const firstName = user.firstName || '';
        const lastName = user.lastName || '';
        return `${firstName} ${lastName}`.trim() || 'User';
    };

    const unreadNotifications = notifications.filter(n => !n.isRead);

    // Error state
    if (error) {
        return (
            <nav className="px-6 py-4 flex justify-between items-center md:px-16 lg:px-24 border-b border-white/10 bg-gradient-to-r from-blue-900/50 to-indigo-900/50">
                <div className="flex items-center space-x-3">
                    <DollarSign size={24} className="text-emerald-400" />
                    <h1 className="text-2xl font-bold tracking-tight">
                        <span className="text-emerald-400">Fin</span>Wise
                    </h1>
                </div>
                <div className="text-red-400 text-sm">
                    Error: {error}
                </div>
            </nav>
        );
    }

    // Loading state
    if (loading) {
        return (
            <nav className="px-6 py-4 flex justify-between items-center md:px-16 lg:px-24 border-b border-white/10 bg-gradient-to-r from-blue-900/50 to-indigo-900/50">
                <div className="flex items-center space-x-3">
                    <DollarSign size={24} className="text-emerald-400" />
                    <h1 className="text-2xl font-bold tracking-tight">
                        <span className="text-emerald-400">Fin</span>Wise
                    </h1>
                </div>
                <div className="animate-pulse flex items-center space-x-4">
                    <div className="h-8 w-8 bg-gray-600 rounded-full"></div>
                    <div className="h-4 w-20 bg-gray-600 rounded"></div>
                </div>
            </nav>
        );
    }

    return (
        <nav className="px-6 py-4 flex justify-between items-center md:px-16 lg:px-24 border-b border-white/10 bg-gradient-to-r from-blue-900/50 to-indigo-900/50 backdrop-blur-sm">
            {/* Logo and Brand */}
            <div className="flex items-center space-x-3">
                <DollarSign size={24} className="text-emerald-400" />
                <h1 className="text-2xl font-bold tracking-tight">
                    <span className="text-emerald-400">Fin</span>Wise
                </h1>
            </div>

            {/* Navigation Links */}
            <div className="hidden md:flex items-center space-x-6">
                <button
                    onClick={() => navigate('/dashboard')}
                    className="flex items-center space-x-2 text-gray-300 hover:text-white transition-colors"
                >
                    <Home size={18} />
                    <span>Dashboard</span>
                </button>
                <button
                    onClick={() => navigate('/investments')}
                    className="flex items-center space-x-2 text-gray-300 hover:text-white transition-colors"
                >
                    <PieChart size={18} />
                    <span>Investments</span>
                </button>
            </div>

            {/* Right Side - User Info and Actions */}
            <div className="flex items-center space-x-4">
                {/* Notifications */}
                <div className="relative">
                    <button
                        className="p-2 rounded-full hover:bg-white/5 relative transition-colors"
                        onClick={() => setShowNotifications(!showNotifications)}
                    >
                        <Bell size={20} className="text-gray-300" />
                        {unreadNotifications.length > 0 && (
                            <span className="absolute -top-1 -right-1 h-5 w-5 bg-red-500 rounded-full text-xs flex items-center justify-center text-white font-semibold">
                                {unreadNotifications.length > 9 ? '9+' : unreadNotifications.length}
                            </span>
                        )}
                    </button>

                    {/* Notifications Dropdown */}
                    {showNotifications && (
                        <div className="absolute right-0 mt-2 w-80 bg-gray-800 rounded-lg shadow-xl border border-gray-700 z-50">
                            <div className="p-4 border-b border-gray-700">
                                <h3 className="text-white font-semibold">Notifications</h3>
                            </div>
                            <div className="max-h-64 overflow-y-auto">
                                {notifications.length === 0 ? (
                                    <div className="p-4 text-gray-400 text-center">
                                        No notifications
                                    </div>
                                ) : (
                                    notifications.slice(0, 5).map(notification => (
                                        <div
                                            key={notification.id}
                                            className={`p-3 border-b border-gray-700 hover:bg-gray-700/50 cursor-pointer ${
                                                !notification.isRead ? 'bg-blue-900/20' : ''
                                            }`}
                                            onClick={() => markNotificationAsRead(notification.id)}
                                        >
                                            <div className="flex items-start space-x-3">
                                                <span className="text-lg">
                                                    {getNotificationIcon(notification.type)}
                                                </span>
                                                <div className="flex-1">
                                                    <p className="text-white font-medium text-sm">
                                                        {notification.title}
                                                    </p>
                                                    <p className="text-gray-400 text-xs">
                                                        {notification.message}
                                                    </p>
                                                </div>
                                                {!notification.isRead && (
                                                    <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
                                                )}
                                            </div>
                                        </div>
                                    ))
                                )}
                            </div>
                        </div>
                    )}
                </div>

                {/* Settings */}
                <button
                    className="p-2 rounded-full hover:bg-white/5 transition-colors"
                    onClick={() => navigate('/settings')}
                >
                    <Settings size={20} className="text-gray-300" />
                </button>

                {/* User Profile Section */}
                <div className="relative">
                    <button
                        className="flex items-center space-x-3 hover:bg-white/5 rounded-lg p-2 transition-colors"
                        onClick={() => setShowProfileMenu(!showProfileMenu)}
                    >
                        {/* Profile Picture - Google Image */}
                        <div className="relative">
                            {userProfile?.image ? (
                                <img
                                    src={userProfile.image}
                                    alt="Profile"
                                    className="h-8 w-8 rounded-full border-2 border-emerald-400/50 object-cover"
                                    onError={(e) => {
                                        console.log('Image failed to load:', userProfile.image);
                                        e.target.style.display = 'none';
                                        e.target.nextSibling.style.display = 'flex';
                                    }}
                                    onLoad={() => {
                                        console.log('Google profile image loaded successfully');
                                    }}
                                />
                            ) : null}
                            <div
                                className="h-8 w-8 rounded-full bg-emerald-500/20 flex items-center justify-center text-emerald-400"
                                style={{ display: userProfile?.image ? 'none' : 'flex' }}
                            >
                                <User size={18} />
                            </div>
                        </div>

                        {/* User Info */}
                        <div className="text-left hidden md:block">
                            <div className="text-sm font-medium text-gray-300">
                                {getFullName(userProfile)}
                            </div>
                            {familyProfile && (
                                <div className="flex items-center text-xs text-gray-400">
                                    <Users size={10} className="mr-1" />
                                    {familyProfile.familySize} members
                                    {familyProfile.location && (
                                        <>
                                            <MapPin size={10} className="ml-2 mr-1" />
                                            {familyProfile.location}
                                        </>
                                    )}
                                </div>
                            )}
                        </div>

                        <ChevronDown size={16} className="text-gray-400" />
                    </button>

                    {/* Profile Dropdown Menu */}
                    {showProfileMenu && (
                        <div className="absolute right-0 mt-2 w-64 bg-gray-800 rounded-lg shadow-xl border border-gray-700 z-50">
                            {/* Profile Header */}
                            <div className="p-4 border-b border-gray-700">
                                <div className="flex items-center space-x-3">
                                    {userProfile?.image ? (
                                        <img
                                            src={userProfile.image}
                                            alt="Profile"
                                            className="h-12 w-12 rounded-full border-2 border-emerald-400/50 object-cover"
                                            onError={(e) => {
                                                e.target.style.display = 'none';
                                                e.target.nextSibling.style.display = 'flex';
                                            }}
                                        />
                                    ) : null}
                                    <div
                                        className="h-12 w-12 rounded-full bg-emerald-500/20 flex items-center justify-center text-emerald-400"
                                        style={{ display: userProfile?.image ? 'none' : 'flex' }}
                                    >
                                        <User size={24} />
                                    </div>
                                    <div>
                                        <p className="text-white font-semibold">
                                            {getFullName(userProfile)}
                                        </p>
                                        <p className="text-gray-400 text-sm">
                                            {userProfile?.email || 'user@example.com'}
                                        </p>
                                    </div>
                                </div>

                                {familyProfile && (
                                    <div className="mt-3 p-2 bg-emerald-500/10 rounded-lg">
                                        <div className="text-xs text-emerald-400 font-semibold mb-1">
                                            Family Profile
                                        </div>
                                        <div className="text-xs text-gray-300">
                                            Monthly Savings: ₹{(familyProfile.monthlyIncome - familyProfile.monthlyExpenses).toLocaleString()}
                                        </div>
                                        <div className="text-xs text-gray-400">
                                            Risk Tolerance: {familyProfile.riskTolerance}
                                        </div>
                                    </div>
                                )}
                            </div>

                            {/* Menu Items */}
                            <div className="py-2">
                                <button
                                    className="w-full text-left px-4 py-2 text-gray-300 hover:bg-gray-700 hover:text-white transition-colors"
                                    onClick={() => {
                                        navigate('/profile');
                                        setShowProfileMenu(false);
                                    }}
                                >
                                    View Profile
                                </button>
                                <button
                                    className="w-full text-left px-4 py-2 text-gray-300 hover:bg-gray-700 hover:text-white transition-colors"
                                    onClick={() => {
                                        navigate('/family-details');
                                        setShowProfileMenu(false);
                                    }}
                                >
                                    Edit Family Profile
                                </button>
                                <button
                                    className="w-full text-left px-4 py-2 text-gray-300 hover:bg-gray-700 hover:text-white transition-colors"
                                    onClick={() => {
                                        navigate('/settings');
                                        setShowProfileMenu(false);
                                    }}
                                >
                                    Settings
                                </button>
                                <hr className="my-2 border-gray-700" />
                                <button
                                    className="w-full text-left px-4 py-2 text-red-400 hover:bg-red-900/20 hover:text-red-300 transition-colors flex items-center space-x-2"
                                    onClick={handleLogout}
                                >
                                    <LogOut size={16} />
                                    <span>Sign Out</span>
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            </div>

            {/* Click outside to close dropdowns */}
            {(showProfileMenu || showNotifications) && (
                <div
                    className="fixed inset-0 z-40"
                    onClick={() => {
                        setShowProfileMenu(false);
                        setShowNotifications(false);
                    }}
                />
            )}
        </nav>
    );
};

export default DynamicNavbar;
