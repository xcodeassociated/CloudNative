import React, { Component } from 'react';
import configureStore from './store/configure'
import Home from './components/Home';
import NavBar from "./components/NavBar";
import RouteController from "./routes/RouteController";

// routes:
export const HomeRoute = () => <Home />;

class App extends Component {

  public store = (): object => {
    const store = Object.assign({}, this.props);
    return configureStore(store);
  };

  public render(): JSX.Element {
    return (
        <div>
          <NavBar />
          <RouteController>{this.store()}</RouteController>
        </div>
    );

  }
}

export default App;
