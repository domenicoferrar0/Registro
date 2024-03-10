// Function to retrieve JWT token from local storage
function getToken() {
    return localStorage.getItem('jwt');
}

// Axios request interceptor to add JWT token to request headers
axios.interceptors.request.use(function(config) {
    const token = getToken();
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, function(error) {
    return Promise.reject(error);
});