import React, { useState } from 'react';
import { User, Calendar, DollarSign, Heart, Edit3, Trash2, Target } from 'lucide-react';

const blueGradient = 'linear-gradient(135deg, #1a237e 0%, #283593 60%, #1565c0 100%)';
const colors = {
    card: 'rgba(255,255,255,0.07)',
    border: 'rgba(255,255,255,0.08)',
    white: '#fff',
    green: '#43a047',
    blue: '#1976d2',
    orange: '#f9a825',
    purple: '#7c4dff',
    red: '#e53935',
    text: '#fff',
    label: '#b3c2f2',
};

const initialPlans = [
    {
        id: 1,
        plan_name: "Priya's Wedding",
        for_name: "Priya",
        relationship: "Daughter",
        estimated_year: 2027,
        estimated_total: 1200000,
        current_savings: 200000,
        monthly_contrib: 15000,
        inflation_rate: 6,
        notes: "Royal wedding plan"
    }
];

function getProgress(plan) {
    const years = plan.estimated_year - new Date().getFullYear();
    const futureValue = plan.current_savings * Math.pow(1 + plan.inflation_rate / 100, years) +
        plan.monthly_contrib * ((Math.pow(1 + (plan.inflation_rate / 100) / 12, years * 12) - 1) / ((plan.inflation_rate / 100) / 12));
    const inflatedCost = plan.estimated_total * Math.pow(1 + plan.inflation_rate / 100, years);
    return Math.min(100, (futureValue / inflatedCost) * 100);
}

function getShortfall(plan) {
    const years = plan.estimated_year - new Date().getFullYear();
    const futureValue = plan.current_savings * Math.pow(1 + plan.inflation_rate / 100, years) +
        plan.monthly_contrib * ((Math.pow(1 + (plan.inflation_rate / 100) / 12, years * 12) - 1) / ((plan.inflation_rate / 100) / 12));
    const inflatedCost = plan.estimated_total * Math.pow(1 + plan.inflation_rate / 100, years);
    return Math.max(0, inflatedCost - futureValue);
}

export default function MarriagePage() {
    const [marriagePlans, setMarriagePlans] = useState(initialPlans);

    // Summary
    const totalPlans = marriagePlans.length;
    const totalEstimated = marriagePlans.reduce((sum, plan) => sum + Number(plan.estimated_total || 0), 0);
    const totalSavings = marriagePlans.reduce((sum, plan) => sum + Number(plan.current_savings || 0), 0);

    // Add Plan Modal (demo only, not implemented)
    // ... You can add a modal/form here as needed ...

    return (
        <div
            style={{
                minHeight: '100vh',
                background: blueGradient,
                padding: '32px 0'
            }}
        >
            <div style={{ maxWidth: 1100, margin: '0 auto', padding: '0 24px' }}>
                {/* Header */}
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
                    <div>
                        <h2 style={{ color: colors.white, fontWeight: 700, fontSize: 32, margin: 0 }}>
                            <Heart size={32} style={{ verticalAlign: 'middle', marginRight: 10 }} />
                            Marriage Planning
                        </h2>
                        <div style={{ color: colors.label, fontSize: 18, marginTop: 5 }}>
                            Plan and track your family's marriage expenses
                        </div>
                    </div>
                    <button style={{
                        background: colors.green,
                        color: '#fff',
                        fontWeight: 600,
                        fontSize: 18,
                        border: 'none',
                        borderRadius: 8,
                        padding: '12px 28px',
                        cursor: 'pointer',
                        boxShadow: '0 2px 8px rgba(60,72,100,0.07)'
                    }}>
                        + Add Plan
                    </button>
                </div>

                {/* Summary Cards */}
                <div style={{
                    display: 'flex',
                    gap: 24,
                    marginBottom: 38,
                    flexWrap: 'wrap'
                }}>
                    <div style={{
                        flex: 1,
                        minWidth: 220,
                        background: colors.card,
                        borderRadius: 18,
                        border: `1px solid ${colors.border}`,
                        padding: 24,
                        color: colors.text,
                        boxShadow: '0 2px 12px 0 rgba(20,40,80,0.06)'
                    }}>
                        <User size={28} color={colors.blue} style={{ marginBottom: 12 }} />
                        <div style={{ fontSize: 24, fontWeight: 700 }}>₹{totalSavings.toLocaleString()}</div>
                        <div style={{ color: colors.label, fontSize: 16, marginTop: 6 }}>Total Current Savings</div>
                    </div>
                    <div style={{
                        flex: 1,
                        minWidth: 220,
                        background: colors.card,
                        borderRadius: 18,
                        border: `1px solid ${colors.border}`,
                        padding: 24,
                        color: colors.text,
                        boxShadow: '0 2px 12px 0 rgba(20,40,80,0.06)'
                    }}>
                        <DollarSign size={28} color={colors.green} style={{ marginBottom: 12 }} />
                        <div style={{ fontSize: 24, fontWeight: 700 }}>₹{marriagePlans.reduce((sum, p) => sum + Number(p.monthly_contrib || 0), 0).toLocaleString()}</div>
                        <div style={{ color: colors.label, fontSize: 16, marginTop: 6 }}>Monthly Contributions</div>
                    </div>
                    <div style={{
                        flex: 1,
                        minWidth: 220,
                        background: colors.card,
                        borderRadius: 18,
                        border: `1px solid ${colors.border}`,
                        padding: 24,
                        color: colors.text,
                        boxShadow: '0 2px 12px 0 rgba(20,40,80,0.06)'
                    }}>
                        <Target size={28} color={colors.orange} style={{ marginBottom: 12 }} />
                        <div style={{ fontSize: 24, fontWeight: 700 }}>₹{(totalEstimated / 100000).toFixed(1)}L</div>
                        <div style={{ color: colors.label, fontSize: 16, marginTop: 6 }}>Total Planned Expenses</div>
                    </div>
                    <div style={{
                        flex: 1,
                        minWidth: 220,
                        background: colors.card,
                        borderRadius: 18,
                        border: `1px solid ${colors.border}`,
                        padding: 24,
                        color: colors.text,
                        boxShadow: '0 2px 12px 0 rgba(20,40,80,0.06)'
                    }}>
                        <Heart size={28} color={colors.purple} style={{ marginBottom: 12 }} />
                        <div style={{ fontSize: 24, fontWeight: 700 }}>{totalPlans}</div>
                        <div style={{ color: colors.label, fontSize: 16, marginTop: 6 }}>Plans Created</div>
                    </div>
                </div>

                {/* Plan Details Cards */}
                {marriagePlans.map(plan => {
                    const progress = getProgress(plan);
                    const shortfall = getShortfall(plan);
                    return (
                        <div key={plan.id} style={{
                            background: colors.card,
                            borderRadius: 18,
                            border: `1px solid ${colors.border}`,
                            padding: 28,
                            marginBottom: 32,
                            color: colors.text,
                            boxShadow: '0 2px 12px 0 rgba(20,40,80,0.06)',
                            maxWidth: 600
                        }}>
                            <div style={{ display: 'flex', alignItems: 'center', marginBottom: 10 }}>
                                <div style={{
                                    background: colors.purple,
                                    borderRadius: '50%',
                                    width: 44,
                                    height: 44,
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    marginRight: 16
                                }}>
                                    <User size={24} color="#fff" />
                                </div>
                                <div>
                                    <div style={{ fontWeight: 700, fontSize: 22, color: colors.white }}>{plan.for_name}</div>
                                    <div style={{ color: colors.label, fontSize: 16 }}>Relationship: {plan.relationship}</div>
                                </div>
                                <div style={{ marginLeft: 'auto', display: 'flex', gap: 10 }}>
                                    <button style={{
                                        background: colors.blue, border: 'none', borderRadius: 6, padding: 8, cursor: 'pointer'
                                    }}>
                                        <Edit3 size={18} color="#fff" />
                                    </button>
                                    <button style={{
                                        background: colors.red, border: 'none', borderRadius: 6, padding: 8, cursor: 'pointer'
                                    }}>
                                        <Trash2 size={18} color="#fff" />
                                    </button>
                                </div>
                            </div>
                            <div style={{ fontSize: 16, marginBottom: 8 }}>
                                <span style={{ color: colors.label }}>Year:</span> {plan.estimated_year} &nbsp; | &nbsp;
                                <span style={{ color: colors.label }}>Estimated Cost:</span> ₹{(plan.estimated_total / 100000).toFixed(1)}L
                            </div>
                            <div style={{ fontSize: 16, marginBottom: 8 }}>
                                <span style={{ color: colors.label }}>Current Savings:</span> ₹{(plan.current_savings / 100000).toFixed(2)}L &nbsp; | &nbsp;
                                <span style={{ color: colors.label }}>Monthly:</span> ₹{plan.monthly_contrib.toLocaleString()}
                            </div>
                            <div style={{ fontSize: 16, marginBottom: 8 }}>
                                <span style={{ color: colors.label }}>Inflation Rate:</span> {plan.inflation_rate}%
                            </div>
                            <div style={{ fontSize: 16, marginBottom: 8 }}>
                                <span style={{ color: colors.label }}>Notes:</span> {plan.notes}
                            </div>
                            {/* Progress Bar */}
                            <div style={{ marginTop: 18, marginBottom: 10 }}>
                                <div style={{ color: colors.label, marginBottom: 3, fontSize: 15 }}>Savings Progress</div>
                                <div style={{
                                    height: 7,
                                    borderRadius: 4,
                                    background: '#222a5e',
                                    overflow: 'hidden',
                                    marginBottom: 6
                                }}>
                                    <div style={{
                                        width: `${progress}%`,
                                        height: '100%',
                                        background: colors.red,
                                        transition: 'width 0.4s'
                                    }} />
                                </div>
                                <span style={{ color: colors.red, fontSize: 15 }}>{progress.toFixed(1)}%</span>
                            </div>
                            {/* Shortfall Alert */}
                            {shortfall > 0 && (
                                <div style={{
                                    background: 'rgba(229,57,53,0.14)',
                                    border: `1px solid ${colors.red}`,
                                    borderRadius: 8,
                                    padding: 12,
                                    color: colors.red,
                                    fontWeight: 600,
                                    fontSize: 15,
                                    marginTop: 12
                                }}>
                                    Shortfall: ₹{(shortfall / 100000).toFixed(1)}L
                                    <div style={{ color: '#ff8a65', fontWeight: 400, fontSize: 14 }}>
                                        Consider increasing monthly contribution by ₹{Math.ceil(shortfall / ((plan.estimated_year - new Date().getFullYear()) * 12) / 1000) * 1000}
                                    </div>
                                </div>
                            )}
                        </div>
                    );
                })}
            </div>
        </div>
    );
}
