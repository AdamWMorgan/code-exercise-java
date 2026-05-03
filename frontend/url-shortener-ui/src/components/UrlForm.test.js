import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import UrlForm from './UrlForm';

describe('UrlForm Component', () => {
  const mockProps = {
    fullUrl: '',
    setFullUrl: jest.fn(),
    alias: '',
    setAlias: jest.fn(),
    onSubmit: jest.fn((e) => e.preventDefault()),
    error: '',
    history: [],
    onDelete: jest.fn()
  };

  const mockHistory = [
    { alias: 'google', fullUrl: 'https://google.com', shortUrl: 'http://localhost:8080/google' },
    { alias: 'bbc', fullUrl: 'https://bbc.com', shortUrl: 'http://localhost:8080/bbc' }
  ];

  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renders form fields and submit button', () => {
    render(<UrlForm {...mockProps} />);
    
    expect(screen.getByLabelText(/Full URL/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Custom Alias/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Shorten URL/i })).toBeInTheDocument();
  });

  test('submit button is disabled when fullUrl is empty', () => {
    render(<UrlForm {...mockProps} fullUrl="" />);
    const button = screen.getByRole('button', { name: /Shorten URL/i });
    expect(button).toBeDisabled();
  });

  test('calls setFullUrl and setAlias on input change', () => {
    render(<UrlForm {...mockProps} />);
    
    const urlInput = screen.getByLabelText(/Full URL/i);
    const aliasInput = screen.getByLabelText(/Custom Alias/i);

    fireEvent.change(urlInput, { target: { value: 'https://test.com' } });
    expect(mockProps.setFullUrl).toHaveBeenCalledWith('https://test.com');

    fireEvent.change(aliasInput, { target: { value: 'test-alias' } });
    expect(mockProps.setAlias).toHaveBeenCalledWith('test-alias');
  });

  test('calls onSubmit when form is submitted', () => {
    render(<UrlForm {...mockProps} fullUrl="https://test.com" />);
    
    const submitButton = screen.getByRole('button', { name: /Shorten URL/i });
    fireEvent.submit(submitButton);

    expect(mockProps.onSubmit).toHaveBeenCalled();
  });

  test('displays error message when error prop is provided', () => {
    const errorMessage = 'Invalid URL provided';
    render(<UrlForm {...mockProps} error={errorMessage} />);
    
    expect(screen.getByText(errorMessage)).toBeInTheDocument();
    expect(screen.getByRole('alert')).toBeInTheDocument();
  });

  test('renders history table items correctly', () => {
    render(<UrlForm {...mockProps} history={mockHistory} />);
    
    expect(screen.getByText('google')).toBeInTheDocument();
    expect(screen.getByText('https://google.com')).toBeInTheDocument();
    expect(screen.getByText('http://localhost:8080/google')).toBeInTheDocument();
    
    expect(screen.getByText('bbc')).toBeInTheDocument();
  });

  test('calls onDelete when delete button is clicked', () => {
    render(<UrlForm {...mockProps} history={mockHistory} />);
    
    const deleteButtons = screen.getAllByRole('button');
    fireEvent.click(deleteButtons[1]); 

    expect(mockProps.onDelete).toHaveBeenCalledWith('google');
  });

  test('displays empty state message when history is empty', () => {
    render(<UrlForm {...mockProps} history={[]} />);
    
    expect(screen.getByText(/No URLs shortened yet!/i)).toBeInTheDocument();
  });
});