import React, { useState, useEffect } from 'react';
import { Container, Typography } from '@mui/material';
import { shortenUrl, getAllUrls, deleteUrl } from './api';
import UrlForm from './components/UrlForm';

function App() {
  const [fullUrl, setFullUrl] = useState('');
  const [alias, setAlias] = useState('');
  const [history, setHistory] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchHistory();
  }, []);

  const fetchHistory = async () => {
    try {
      const data = await getAllUrls();
      setHistory(data);
    } catch (err) {
      console.error('Failed to fetch history:', err);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await shortenUrl(fullUrl, alias);
      setFullUrl('');
      setAlias('');
      fetchHistory();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDelete = async (aliasToDelete) => {
    if (window.confirm(`Delete alias "${aliasToDelete}"?`)) {
      try {
        await deleteUrl(aliasToDelete);
        fetchHistory();
      } catch (err) {
        setError('Failed to delete link');
      }
    }
  };

  return (
    <Container maxWidth="md" sx={{ mt: 8, mb: 8 }}>
      <Typography variant="h3" align="center" gutterBottom fontWeight="bold">
        URL Shortener
      </Typography>
      
      <UrlForm 
        fullUrl={fullUrl}
        setFullUrl={setFullUrl}
        alias={alias}
        setAlias={setAlias}
        onSubmit={handleSubmit}
        onDelete={handleDelete}
        history={history}
        error={error}
      />
    </Container>
  );
}

export default App;