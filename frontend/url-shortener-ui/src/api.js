const BASE_URL = 'http://localhost:8080';

export const shortenUrl = async (fullUrl, customAlias = '') => {
  const payload = { fullUrl };
  if (customAlias) payload.customAlias = customAlias;

  const response = await fetch(`${BASE_URL}/shorten`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error.message || 'Invalid input or alias already taken');
  }

  return response.json();
};

export const getAllUrls = async () => {
  const response = await fetch(`${BASE_URL}/urls`);
  if (!response.ok) throw new Error('Failed to fetch URLs');
  return response.json();
};

export const deleteUrl = async (alias) => {
  const response = await fetch(`${BASE_URL}/${alias}`, {
    method: 'DELETE',
  });
  if (!response.ok) throw new Error('Failed to delete alias');
  return true;
};