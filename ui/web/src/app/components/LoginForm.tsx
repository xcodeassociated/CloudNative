import React, {Component} from 'react';
import {connect} from 'react-redux';
import {loginAction} from '../store/actions/loginAction'
import {Redirect} from 'react-router-dom'

interface IProps {
  isLoginPending: boolean;
  isLoginSuccess: boolean;
  loginError: string;

  login(email: string, password: string): void;

  dispatch: Function;
}

interface IState {
  email: string;
  password: string;
}

class LoginForm extends Component<IProps, IState> {

  constructor(props: IProps) {
    super(props);
    this.state = {email: '', password: ''};
    this.onSubmit = this.onSubmit.bind(this);
  }

  public isLoggedIn(): boolean {
    return localStorage.getItem('token') != null;
  };

  public render() {
    let {email, password} = this.state;
    let {isLoginPending, loginError} = this.props;

    if (this.isLoggedIn()) {
      return (<Redirect to='/'/>);
    } else {
      return (
        <div className="col-md-6 col-md-offset-3">
          <h2>Login</h2>
          <form name="form" onSubmit={this.onSubmit}>
            <div className="form-group-collection">
              <div className="form-group">
                <label>Email:</label>
                <input disabled={isLoginPending} type="text" className="form-control" name="email"
                       onChange={e => this.setState({email: e.target.value})} value={email}/>
              </div>

              <div className="form-group">
                <label>Password:</label>
                <input disabled={isLoginPending} type="password" className="form-control" name="password"
                       onChange={e => this.setState({password: e.target.value})} value={password}/>
              </div>
            </div>

            <div className="form-group">
              <button disabled={isLoginPending} className="btn btn-primary">Login</button>
            </div>

            <div className="message">
              {isLoginPending && <div>Please wait...</div>}
              {loginError && <div>{loginError}</div>}
            </div>
          </form>
        </div>
      )
    }
  }

  public onSubmit(e) {
    e.preventDefault();
    let {email, password} = this.state;
    this.props.dispatch(loginAction(email, password));
  }
}

const mapStateToProps = (state) => {
  return {
    isLoginPending: state.isLoginPending,
    isLoginSuccess: state.isLoginSuccess,
    loginError: state.loginError
  };
};

export default connect(mapStateToProps)(LoginForm);
