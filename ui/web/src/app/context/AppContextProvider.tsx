import React, {Component, ReactChild} from "react";
import {Provider} from 'react-redux';
import AppContext from "./AppContext";

type NamedProps = {
  data?: any
}

type Props = {
  children: ReactChild | NamedProps
}

class AppContextProvider extends Component<Props> {

  public render() {
    return (
      <Provider store={this.props.children}>
        <AppContext children={this.props.children}/>
      </Provider>
    )
  }
}

export default AppContextProvider;
