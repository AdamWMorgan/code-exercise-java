import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import App from './App';
import * as api from './api';

jest.mock('./api');

describe('App Component', () => {
  const mockHistory = [
    { alias: 'google', fullUrl: 'https://google.com', shortUrl: 'http://localhost:8080/google' },
    { alias: 'bbc', fullUrl: 'https://bbc.com', shortUrl: 'http://localhost:8080/bbc' }
  ];

  beforeEach(() => {
    jest.clearAllMocks();
    api.getAllUrls.mockResolvedValue(mockHistory);
  });

  test('renders title and fetches history on mount', async () => {
    render(<App />);
    
    expect(screen.getByText(/URL Shortener/i)).toBeInTheDocument();
    
    await waitFor(() => {
      expect(api.getAllUrls).toHaveBeenCalled();
      expect(screen.getByText('google')).toBeInTheDocument();
      expect(screen.getByText('bbc')).toBeInTheDocument();
    });
  });

  test('successfully submits the form and refreshes history', async () => {
    api.shortenUrl.mockResolvedValue({ alias: 'new-link' });
    
    render(<App />);

    await waitFor(() => expect(api.getAllUrls).toHaveBeenCalled());

    const urlInput = screen.getByLabelText(/Full URL/i);
    fireEvent.change(urlInput, { target: { value: 'https://example.com' } });
    
    const submitButton = screen.getByRole('button', { name: /Shorten URL/i });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(api.shortenUrl).toHaveBeenCalledWith('https://example.com', '');
      expect(api.getAllUrls).toHaveBeenCalledTimes(2);
    });
  });

  test('handles deletion after confirmation', async () => {
    const confirmSpy = jest.spyOn(window, 'confirm').mockImplementation(() => true);
    api.deleteUrl.mockResolvedValue({});

    render(<App />);

    await waitFor(() => expect(screen.getByText('google')).toBeInTheDocument());

    const deleteButtons = screen.getAllByTestId('DeleteIcon');
    fireEvent.click(deleteButtons[0].closest('button'));

    expect(confirmSpy).toHaveBeenCalled();
    await waitFor(() => {
      expect(api.deleteUrl).toHaveBeenCalledWith('google');
      expect(api.getAllUrls).toHaveBeenCalledTimes(2);
    });

    confirmSpy.mockRestore();
  });

  test('displays error message when API fails', async () => {
    const errorMessage = 'Something went wrong';
    api.shortenUrl.mockRejectedValue(new Error(errorMessage));

    render(<App />);

    await waitFor(() => expect(api.getAllUrls).toHaveBeenCalled());

    const urlInput = screen.getByLabelText(/Full URL/i);
    fireEvent.change(urlInput, { target: { value: 'https://epicfail.com' } });
    
    fireEvent.click(screen.getByRole('button', { name: /Shorten URL/i }));

    await waitFor(() => {
      expect(screen.getByText(errorMessage)).toBeInTheDocument();
    });
  });
});