import {
    BrowserRouter as Router,
    Routes,
    Route
} from "react-router-dom";
import Dashboard from './components/Dashboard';
import App from './components/landing';

function Main() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<App />} />
                <Route path="/dashboard" element={<Dashboard />} />
            </Routes>
        </Router>
    );
}

export default Main;
