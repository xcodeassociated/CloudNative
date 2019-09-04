import React, { Component } from 'react';
import configureStore from '../store/config/storeConfig'
import AppRouterProvider from "../routes/AppRouterProvider";
import '../style/App.css';

class App extends Component {

  public store = (): object => {
    const store = Object.assign({}, this.props);
    return configureStore(store);
  };

  public render(): JSX.Element {
    return (
        <div id="app">
          <AppRouterProvider children={this.store()} />
        </div>
    );

  }
}

export default App;
