import * as React from "react";
import {Component, ReactChild} from "react";
import {Redirect} from 'react-router-dom'
import {logoutAction} from '../store/actions/loginAction'
import {connect} from 'react-redux';

type NamedProps = {
  data?: any
}

type PropsAppRouter = {
  children: ReactChild | NamedProps
  dispatch: Function;
}

class Logout extends Component<PropsAppRouter> {

  constructor(props: Readonly<PropsAppRouter>) {
    super(props);
    this.state = {};
  }

  public render() {
    this.props.dispatch(logoutAction());
    return (<Redirect to='/'/>);
  }
}

const mapState = (state) => {
  return {
    ...state
  };
};

export default connect(mapState)(Logout);
