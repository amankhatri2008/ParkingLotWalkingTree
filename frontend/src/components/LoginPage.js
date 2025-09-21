import React from 'react';

const LoginPage = () => {
  const handleLogin = () => {
    // Redirect to the backend's Google OAuth2 endpoint
    window.location.href = `${process.env.REACT_APP_API_URL}/api/oauth2/authorization/google`;
  };

  return (
     <div className="container">
      <img src="/Walkingtree-4-min.png" alt="Parking Lot Logo" style={{ display: 'block', margin: '0 auto', marginTop: '100px' }} />
      <h1 style={{ textAlign: 'center' }}>Parking Lot Management</h1>
      <div style={{ marginBottom: '20px', padding: '15px' }}></div>

      <h1 style={{ textAlign: 'center' }}><button onClick={handleLogin} className='center'>
        <img src="/googleSignIn.JPG" alt="Google Logo" style={{ width: '550px', height: '250px'}}>
    
        </img></button></h1>
      
    </div>
  );
};

export default LoginPage;