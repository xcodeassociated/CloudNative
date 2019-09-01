import {config} from '../config/config'

export const loginService = {
  login
};

function login(username, password): Promise<Response> {
  const requestOptions = {
    method: 'POST',
    headers: [['Content-Type', 'application/json'], ['Accept', 'application/json']],
    body: JSON.stringify({
       username: username, password: password
    })
  };

  return fetch(`${config.backend_url}/api/gateway/resource/login`, requestOptions)
      .catch(() => {
        return Promise.reject('Backend not reachable');
      })
      .then(function (response: Response): any {
        if (!response.ok) {
          return Promise.reject(response.statusText);
        } else {
          return response.json();
        }
      });
}