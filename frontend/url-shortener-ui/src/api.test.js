import { shortenUrl, getAllUrls, deleteUrl } from './api';

global.fetch = jest.fn();

describe('API Service', () => {
  beforeEach(() => {
    fetch.mockClear();
  });

  describe('shortenUrl', () => {
    test('sends the correct payload and returns data on success', async () => {
      const mockResponse = { alias: 'test', fullUrl: 'https://bbc.com' };
      
      fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockResponse,
      });

      const result = await shortenUrl('https://bbc.com', 'test-alias');

      expect(fetch).toHaveBeenCalledWith(expect.stringContaining('/shorten'), {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ fullUrl: 'https://bbc.com', customAlias: 'test-alias' }),
      });
      expect(result).toEqual(mockResponse);
    });

    test('throws specific error message from server on failure', async () => {
      fetch.mockResolvedValueOnce({
        ok: false,
        json: async () => ({ message: 'Alias taken' }),
      });

      await expect(shortenUrl('https://bbc.com', 'taken'))
        .rejects.toThrow('Alias taken');
    });
  });

  describe('getAllUrls', () => {
    test('fetches and returns all URLs', async () => {
      const mockList = [{ alias: '1' }, { alias: '2' }];
      fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockList,
      });

      const result = await getAllUrls();
      expect(result).toEqual(mockList);
      expect(fetch).toHaveBeenCalledWith(expect.stringContaining('/urls'));
    });

    test('throws error when fetch fails', async () => {
      fetch.mockResolvedValueOnce({ ok: false });

      await expect(getAllUrls()).rejects.toThrow('Failed to fetch URLs');
    });
  });

  describe('deleteUrl', () => {
    test('calls delete endpoint with correct alias', async () => {
      fetch.mockResolvedValueOnce({ ok: true });

      const result = await deleteUrl('some-alias');
      
      expect(fetch).toHaveBeenCalledWith(expect.stringContaining('/some-alias'), {
        method: 'DELETE',
      });
      expect(result).toBe(true);
    });
  });
});