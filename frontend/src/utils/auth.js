import axios from 'axios';

export const setUserData = async (token) => {
  try {
    // Store the token
    localStorage.setItem('token', token);
    
    // Fetch user data from backend
    const response = await fetch('http://localhost:8080/api/user', {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      credentials: 'include'
    });
    
    if (!response.ok) {
      throw new Error('Failed to fetch user data');
    }
    
    const userData = await response.json();
    
    // Store user data
    localStorage.setItem('user', JSON.stringify({
      id: userData.id,
      name: `${userData.firstName} ${userData.lastName}`,
      email: userData.email,
      imageUrl: userData.profileImageUrl
    }));
    
    return true;
  } catch (error) {
    console.error('Error setting user data:', error);
    clearUserData(); // Clear any partial data on error
    return false;
  }
};

export const clearUserData = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
};

export const getUserData = () => {
  const userStr = localStorage.getItem('user');
  return userStr ? JSON.parse(userStr) : null;
};

export const getToken = () => {
  return localStorage.getItem('token');
};

export const isAuthenticated = () => {
  const token = getToken();
  const user = getUserData();
  return !!(token && user);
};

// Helper function to get auth headers for axios requests
export const getAuthHeaders = () => {
  const token = getToken();
  return token ? {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  } : {};
};

// Create an axios instance with auth headers
export const createAuthenticatedAxios = () => {
  const token = getToken();
  return axios.create({
    baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080',
    headers: getAuthHeaders(),
    withCredentials: true
  });
};
