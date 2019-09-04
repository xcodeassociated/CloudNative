import React, { Component } from 'react';
import { appConfig } from '../config/appConfig'
import '../style/Reservations.css'

interface IState {
  reservations: Array<Reservation>;
}

interface Reservation {
  id: string;
  reservationName: number;
}

class Reservations extends Component<object, IState> {

  constructor(props: object) {
    super(props);
    this.state = {
      reservations: []
    };
  }

  public hasToken(): boolean {
    return localStorage.getItem('token') != null;
  }

  public componentDidMount(): void {
    const token = localStorage.getItem('token');
    if (this.hasToken()) {
      const requestOptions = {
        method: 'GET',
        headers: [['Content-Type', 'application/json'], ['Accept', 'application/json'], ['Authorization', `Bearer ${token}`]],
      };

      const onFetch = (response: Response) => {
        if (response.ok) {
          response.text().then(text => {
            let objects: Array<Reservation> = JSON.parse(text);
            this.setState({reservations: objects})
          })
        }
        // todo: error printing
      };

      fetch(`${appConfig.backend_url}/api/gateway/resource/reservations`, requestOptions).then(onFetch)
    }
  }

  public render() {
    if (this.hasToken()) {
      return (
          <div id="reservations-list">
            <table className="table table-bordered">
              <tbody>
                <tr>
                  {this.state.reservations
                      .map((reservation: Reservation) => <td className="reservation-item">{reservation.reservationName}</td>)}
                  </tr>
              </tbody>
            </table>
          </div>
      )
    } else {
      return (<h1>Unauthorized</h1>)
    }
  }
}

export default Reservations;