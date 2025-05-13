function AdoptationRequests(){
    return(
        <>
        <div className="container p-4">
        <table className="text-center adopt-reqs-table">
                        <thead>
                            <tr>
                                <th scope="col">Pet-Id</th>
                                <th scope="col">Pet Name</th>
                                <th scope="col">Requester</th>
                                <th scope="col">Location</th>
                                <th scope="col">Status</th>
                                <th scope="col">Handle</th>
                            </tr>
                        </thead>
                        <tbody>
                                <tr key={''}>
                                    <th scope="row">1001</th>
                                    <td>ketty</td>
                                    <td>Adam</td>
                                    <td>Cairo</td>
                                    <td>Pending</td>
                                    <td>###</td>
                                </tr>
                                <tr key={''}>
                                    <th scope="row">1001</th>
                                    <td>ketty</td>
                                    <td>Adam</td>
                                    <td>Cairo</td>
                                    <td>Pending</td>
                                    <td>###</td>
                                </tr>
                                <tr key={''}>
                                    <th scope="row">1001</th>
                                    <td>ketty</td>
                                    <td>Adam</td>
                                    <td>Cairo</td>
                                    <td>Pending</td>
                                    <td>###</td>
                                </tr>
                        </tbody>
                    </table>
        </div>
        </>
    );
}
export default AdoptationRequests;