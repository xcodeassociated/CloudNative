import React, { Component } from 'react';
import { appConfig } from '../config/appConfig'
import '../style/Reservations.css'
import { MDBTable, MDBTableBody, MDBTableHead } from 'mdbreact';
import {Reservation} from "../model/Reservation";

interface IState {
  reservations: Array<Reservation>;
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
            <MDBTable>
              <MDBTableHead>
                <tr className="reservations-list-table-header">
                  <th className="reservations-list-table-header-id">ID</th>
                  <th className="reservations-list-table-header-name">Name</th>
                </tr>
              </MDBTableHead>
              <MDBTableBody>
                {this.state.reservations
                    .map((reservation: Reservation, index: number) =>
                        <tr key={index} className="reservation-item">
                          <td>{reservation.id}</td>
                          <td>{reservation.reservationName}</td>
                        </tr>)}
              </MDBTableBody>
          </MDBTable>
          </div>
      );
    } else {
      return (<h1>Unauthorized</h1>)
    }
  }
}

export default Reservations;