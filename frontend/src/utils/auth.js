class Auth {
  constructor(options) {
    this._url = options.url;
    this._headers = options.headers;
  }

  _handleResponse(res) {
    return res.text().then((text) => {
      const data = text ? JSON.parse(text) : {};

      if (!res.ok) {
        return Promise.reject(data?.message || `Error: ${res.status}`);
      }

      return data;
    });
  }

  registration(data) {
    return fetch(`${this._url}/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    }).then(this._handleResponse);
  }

  authentication(data) {
    return fetch(`${this._url}/login`, {
      method: "POST",
      headers: this._headers,
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