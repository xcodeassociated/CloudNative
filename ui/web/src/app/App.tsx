import React, { Component } from 'react';
import configureStore from './store/configure'
import { HashRouter as Router, Route, Link, Switch } from 'react-router-dom'
import Form from "./components/Form";
import { Provider } from 'react-redux';
import Home from './components/Home';

// routes:
export const HomeRoute = () => <Home />;

class App extends Component {

  public store = (): object => {
    const store = Object.assign({}, this.props);
    return configureStore(store);
  };

  public render(): JSX.Element {
    return (
        <Router>
          <div>
            <nav>
              <Link to="/">Home</Link>
              <Link to="/login">Login</Link>
            </nav>
            <Switch>
              <Route exact path="/" component={HomeRoute} />
              <Route exact path="/login" render={() => <Provider store={this.store()}><Form /></Provider > } />
            </Switch>
          </div>
        </Router>
    );

  }
}

export default App;
