// src/utils/userService.js
export const userService = {
    // Get user data from localStorage
    getUser: () => {
        try {
            const user = localStorage.getItem('user');
            return user ? JSON.parse(user) : null;
        } catch (error) {
            console.error('Error parsing user data:', error);
            return null;
        }
    },

    // Check if user is authenticated
    isAuthenticated: () => {
        return localStorage.getItem('isAuthenticated') === 'true' && userService.getUser() !== null;
    },

    // Get specific user properties based on your backend response
    getUserId: () => {
        return localStorage.getItem('userId');
    },

    getUserEmail: () => {
        return localStorage.getItem('userEmail');
    },

    getUserFirstName: () => {
        return localStorage.getItem('userFirstName');
    },

    getUserLastName: () => {
        return localStorage.getItem('userLastName');
    },

    // Get full name
    getUserFullName: () => {
        const firstName = userService.getUserFirstName();
        const lastName = userService.getUserLastName();
        return `${firstName || ''} ${lastName || ''}`.trim();
    },

    getUserImage: () => {
        return localStorage.getItem('userImage');
    },

    // Check if user is new
    isNewUser: () => {
        return localStorage.getItem('isNewUser') === 'true';
    },

    // Get family profile ID (you'll need to add this to your backend response)
    getFamilyProfileId: () => {
        const user = userService.getUser();
        return user?.familyProfileId || null;
    },

    // Update user data
    updateUser: (userData) => {
        localStorage.setItem('user', JSON.stringify(userData));
        localStorage.setItem('userId', userData.id);
        localStorage.setItem('userEmail', userData.email);
        localStorage.setItem('userFirstName', userData.firstName);
        localStorage.setItem('userLastName', userData.lastName);
        localStorage.setItem('userImage', userData.image || '');
        localStorage.setItem('isNewUser', userData.isNewUser);
    },

    // Clear user data (logout)
    clearUser: () => {
        localStorage.removeItem('user');
        localStorage.removeItem('isAuthenticated');
        localStorage.removeItem('userId');
        localStorage.removeItem('userEmail');
        localStorage.removeItem('userFirstName');
        localStorage.removeItem('userLastName');
        localStorage.removeItem('userImage');
        localStorage.removeItem('isNewUser');
    },

    // Update new user status (after completing family setup)
    setUserAsExisting: () => {
        localStorage.setItem('isNewUser', 'false');
        const user = userService.getUser();
        if (user) {
            user.isNewUser = false;
            localStorage.setItem('user', JSON.stringify(user));
        }
    }
};
