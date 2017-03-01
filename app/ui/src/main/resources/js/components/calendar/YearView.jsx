import React from 'react';

class SingleMonth extends React.Component {

    constructor( props ) {
        super( props );
    }

    render() {
        return <div className="card">
            <div className="card-header" style={{ margin: "0pt", padding: "5pt" }}>
                {this.props.month}
            </div>
            <div className="card-block" style={{ margin: "0pt", padding: "0pt" }}>
                <table width="100%" style={{ margin: "0pt", spacing: "0pt", border: "1pt solid  gray" }}>
                    <thead style={{ border: "1pt solid  gray" }}>
                        <tr>
                            <th>M</th>
                            <th>T</th>
                            <th>W</th>
                            <th>T</th>
                            <th>F</th>
                            <th>S</th>
                            <th>S</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>;
    }
}

export default class YearView extends React.Component {

    constructor( props ) {
        super( props );
    }

    render() {
        return <div>
            <h1>2017</h1>
            <div className="row">
                <div className="col-md-4">
                    <SingleMonth month="January" />
                </div>
                <div className="col-md-4">
                    <SingleMonth month="February" />
                </div>
                <div className="col-md-4">
                    <SingleMonth month="March" />
                </div>
            </div>
            <div className="row">
                <div className="col-md-4">
                    <SingleMonth month="April" />
                </div>
                <div className="col-md-4">
                    <SingleMonth month="May" />
                </div>
                <div className="col-md-4">
                    <SingleMonth month="June" />
                </div>
            </div>
            <div className="row">
                <div className="col-md-4">
                    <SingleMonth month="July" />
                </div>
                <div className="col-md-4">
                    <SingleMonth month="August" />
                </div>
                <div className="col-md-4">
                    <SingleMonth month="September" />
                </div>
            </div>
            <div className="row">
                <div className="col-md-4">
                    <SingleMonth month="October" />
                </div>
                <div className="col-md-4">
                    <SingleMonth month="November" />
                </div>
                <div className="col-md-4">
                    <SingleMonth month="December" />
                </div>
            </div>
        </div>;
    }
}