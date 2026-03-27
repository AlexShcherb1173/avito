class Auth {
  constructor(options) {
    this._url = options.url;
    this._headers = options.headers;
  }

  _handleResponse = async (res) => {
    const contentType = res.headers.get("content-type") || "";

    let data = {};

    try {
      if (res.status !== 204) {
        if (contentType.includes("application/json")) {
          data = await res.json();
        } else {
          const text = await res.text();
          data = text ? { message: text } : {};
        }
      }
    } catch {
      data = {};
    }

    if (!res.ok) {
      return Promise.reject(
          data?.message || data?.error || `Error: ${res.status}`
      );
    }

    return data;
  };

  registration(data) {
    return fetch(`${this._url}/register`, {
      method: "POST",
      headers: {
        ...this._headers,
      },
      body: JSON.stringify(data),
    }).then(this._handleResponse);
  }

  authentication(data) {
    return fetch(`${this._url}/login`, {
      method: "POST",
      headers: {
        ...this._headers,
      },
      body: JSON.stringify(data),
    }).then(this._handleResponse);
  }
}

const auth = new Auth({
  url: "/api",
  headers: {
    Accept: "application/json",
    "Content-Type": "application/json",
  },
});

export default auth;