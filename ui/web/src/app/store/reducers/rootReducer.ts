import {actions} from '../actions/loginAction'

export default function rootReducer(state = {}, action) {
  switch (action.type) {
    case actions.SET_LOGIN_PENDING:
      return Object.assign({}, state, {
        isLoginPending: action.isLoginPending
      });

    case actions.SET_LOGIN_SUCCESS:
      return Object.assign({}, state, {
        isLoginSuccess: action.isLoginSuccess
      });

    case actions.SET_LOGIN_ERROR:
      return Object.assign({}, state, {
        loginError: action.loginError
      });

    case actions.LOGOUT:
      return Object.assign({}, state, {
        isLoginPending: false,
        isLoginSuccess: false,
        loginError: null
      });

    default:
      return state;
  }
}
