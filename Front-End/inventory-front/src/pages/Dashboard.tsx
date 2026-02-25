import { useEffect, useState } from "react";
import { api } from "../services/api";

export default function Dashboard() {
    interface ProductionItem {
        product: string;
        quantity: number;
        totalValue: number;
    }

    interface ProductionResponse {
        totalValue: number;
        productionPlan: ProductionItem[];
    }


    const [data, setData] = useState<ProductionResponse | null>(null);
    const [loading, setLoading] = useState(false);
    const [totalProducts, setTotalProducts] = useState<number>(0);
    const [totalRawMaterials, setTotalRawMaterials] = useState<number>(0);
    const [lowStockProducts, setLowStockProducts] = useState<any[]>([]);
    const [producibleProducts, setProducibleProducts] = useState<number>(0);


    async function loadProduction() {
        try {
            setLoading(true);

            const res = await api.get<ProductionResponse>("/production/suggested");
            setData(res.data);

        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    }
    async function loadMetrics() {
    try {
        const p = await api.get("/dashboard/total-products");
        console.log("products", p.data);
        setTotalProducts(p.data.total);

        const r = await api.get("/dashboard/total-raw-materials");
        console.log("raw", r.data);
        setTotalRawMaterials(r.data.total);

        const pp = await api.get("/dashboard/total-producible-products");
        console.log("producible", pp.data);
        setProducibleProducts(pp.data.total);

        const low = await api.get("/dashboard/low-stock-products");
        console.log("low", low.data);
        setLowStockProducts(low.data);

    } catch (err) {
        console.error("Metrics error", err);
    }
}


    useEffect(() => {
        loadProduction();
        loadMetrics();
    }, []);

    if (loading) return <p>loading...</p>;

    return (
        <div className="container">
            <div className="header">
                <h1>Productions</h1>
                <button onClick={loadProduction}>
                    Update
                </button>
            </div>

            <h2>Total: {data?.totalValue}R$</h2>
            <div className="dashboard-cards">

    <div className="card">
        <h3>Total Products</h3>
        <p>{totalProducts}</p>
    </div>


    <div className="card">
        <h3>Total Raw Materials</h3>
        <p>{totalRawMaterials}</p>
    </div>
    
    <div className="card">
        <h3>Producible Products</h3>
        <p>{producibleProducts}</p>
    </div>

    <div className="card">
        <h3>Low Stock Products</h3>
        <p>{lowStockProducts.length}</p>
    </div>
    


</div>
            <table>
                <thead>
                <tr>
                    <th>Product</th>
                    <th>Quantity</th>
                    <th>Price</th>
                </tr>
                </thead>
                <tbody>
                {data?.productionPlan?.map((item: ProductionItem, index: number) => (
                    <tr key={index}>
                        <td>{item.product}</td>
                        <td>{item.quantity}</td>
                        <td>{item.totalValue}R$</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
