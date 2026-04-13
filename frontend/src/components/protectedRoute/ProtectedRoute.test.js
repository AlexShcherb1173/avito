import React from "react";
import { render, screen } from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute";

describe("components/protectedRoute/ProtectedRoute", () => {
    test("should render children when user exists", () => {
        render(
            <MemoryRouter initialEntries={["/profile"]}>
                <Routes>
                    <Route
                        path="/profile"
                        element={
                            <ProtectedRoute user={{ id: 1 }}>
                                <div>Protected content</div>
                            </ProtectedRoute>
                        }
                    />
                </Routes>
            </MemoryRouter>
        );

        expect(screen.getByText("Protected content")).toBeInTheDocument();
    });

    test("should redirect to default path when user does not exist", () => {
        render(
            <MemoryRouter initialEntries={["/profile"]}>
                <Routes>
                    <Route
                        path="/profile"
                        element={
                            <ProtectedRoute user={null}>
                                <div>Protected content</div>
                            </ProtectedRoute>
                        }
                    />
                    <Route path="/" element={<div>Home page</div>} />
                </Routes>
            </MemoryRouter>
        );

        expect(screen.getByText("Home page")).toBeInTheDocument();
        expect(screen.queryByText("Protected content")).not.toBeInTheDocument();
    });

    test("should redirect to custom path when redirectPath is provided", () => {
        render(
            <MemoryRouter initialEntries={["/profile"]}>
                <Routes>
                    <Route
                        path="/profile"
                        element={
                            <ProtectedRoute user={null} redirectPath="/sign-in">
                                <div>Protected content</div>
                            </ProtectedRoute>
                        }
                    />
                    <Route path="/sign-in" element={<div>Sign in page</div>} />
                </Routes>
            </MemoryRouter>
        );

        expect(screen.getByText("Sign in page")).toBeInTheDocument();
        expect(screen.queryByText("Protected content")).not.toBeInTheDocument();
    });
});