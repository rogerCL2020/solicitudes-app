
import { SOLICITUDES_URL } from '../config/api';

async function request(url, options = {}) {
  const response = await fetch(url, {
    ...options,
    headers: {
      ...(options.body ? { 'Content-Type': 'application/json' } : {}),
      ...options.headers,
    },
  });

  if (!response.ok) {
    const message = await response.text();

    throw new Error(
      message || `Error HTTP ${response.status}: ${response.statusText}`,
    );
  }

  return response;
}

async function parseJson(response) {
  if (response.status === 204) {
    return null;
  }

  return response.json();
}

export async function get(url) {
  const response = await request(url);
  return parseJson(response);
}

export async function post(url, data) {
  const response = await request(url, {
    method: 'POST',
    body: JSON.stringify(data),
  });

  return parseJson(response);
}

export async function download(url) {
  const response = await request(url);
  return response.blob();
}

export { SOLICITUDES_URL };
