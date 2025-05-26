import React from 'react';
function EducationPage({ childrenList }) {
    return (
        <div>
            <h2>Education Planning Page</h2>
            {/* Example: List children */}
            <ul>
                {childrenList && childrenList.map((child, idx) => (
                    <li key={idx}>{child.name}</li>
                ))}
            </ul>
        </div>
    );
}
export default EducationPage;
