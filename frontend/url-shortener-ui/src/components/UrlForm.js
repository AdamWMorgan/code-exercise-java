import React from 'react';
import { 
  Paper, Box, TextField, Button, Alert, Typography,
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
  IconButton, Link 
} from '@mui/material';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import DeleteIcon from '@mui/icons-material/Delete';

    const UrlForm = ({ 
      fullUrl, 
      setFullUrl, 
      alias, 
      setAlias, 
      onSubmit, 
      error, 
      history, 
      onDelete 
    }) => {

  return (
    <Box>
      <Paper elevation={3} sx={{ p: 4, mb: 4, borderRadius: 2 }}>
        <Box component="form" onSubmit={onSubmit}>
          <TextField
            fullWidth
            label="Full URL"
            placeholder="https://example.com/long-url"
            value={fullUrl}
            onChange={(e) => setFullUrl(e.target.value)}
            required
            sx={{ mb: 2 }}
          />
          <TextField
            fullWidth
            label="Custom Alias (Optional)"
            placeholder="short-link"
            value={alias}
            onChange={(e) => setAlias(e.target.value)}
            sx={{ mb: 3 }}
          />
          <Button
            fullWidth
            variant="contained"
            size="large"
            type="submit"
            disabled={!fullUrl}
          >
            Shorten URL
          </Button>
        </Box>
        {error && <Alert severity="error" sx={{ mt: 3 }}>{error}</Alert>}
      </Paper>

      <Typography variant="h5" gutterBottom sx={{ fontWeight: 'medium' }}>
        My Shortened URLs
      </Typography>
      <TableContainer component={Paper} elevation={2} sx={{ borderRadius: 2 }}>
        <Table>
          <TableHead sx={{ bgcolor: '#f8f9fa' }}>
            <TableRow>
              <TableCell sx={{ fontWeight: 'bold' }}>Alias</TableCell>
              <TableCell sx={{ fontWeight: 'bold' }}>Original URL</TableCell>
              <TableCell sx={{ fontWeight: 'bold' }}>Short Link</TableCell>
              <TableCell align="right" sx={{ fontWeight: 'bold' }}>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {history.map((item) => (
              <TableRow key={item.alias} hover>
                <TableCell sx={{ fontWeight: 'medium', color: 'primary.main' }}>
                  {item.alias}
                </TableCell>
                <TableCell sx={{ 
                  maxWidth: 200, 
                  overflow: 'hidden', 
                  textOverflow: 'ellipsis', 
                  whiteSpace: 'nowrap' 
                }}>
                  {item.fullUrl}
                </TableCell>
                <TableCell>
                  <Link href={item.shortUrl} target="_blank" rel="noopener">
                    {item.shortUrl}
                  </Link>
                </TableCell>
                <TableCell align="right">
                  <IconButton onClick={() => onDelete(item.alias)} color="error">
                    <DeleteIcon fontSize="small" />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
            {history.length === 0 && (
              <TableRow>
                <TableCell colSpan={4} align="center" sx={{ py: 3 }}>
                  No URLs shortened yet! 
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default UrlForm;