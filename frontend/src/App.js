import {
    BrowserRouter as Router,
    Routes,
    Route,
    Navigate
} from "react-router-dom";
import Landing from './components/landing'; // Fixed import name and path
import FamilyDetails from './components/FamilyDetails';
import Dashboard from './components/Dashboard';

function App() { // Changed function name from main to App
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Landing />} />
                <Route path="/family-details" element={<FamilyDetails />} />
                <Route path="/dashboard/*" element={<Dashboard />} />
                <Route path="*" element={<Navigate to="/" />} />
            </Routes>
        </Router>
    );
}

export default App; // Changed export from main to App