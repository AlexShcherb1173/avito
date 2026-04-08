import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import AddAd from "./AddAd";

const mockedNavigate = jest.fn();

jest.mock("react-router-dom", () => {
    const actual = jest.requireActual("react-router-dom");
    return {
        ...actual,
        useNavigate: () => mockedNavigate,
    };
});

jest.mock("../userForm/UserForm", () => {
    return function MockUserForm({ title, buttonText, onSubmit, children }) {
        return (
            <div>
                <h1>{title}</h1>
                <form onSubmit={onSubmit}>
                    {children}
                    <button type="submit">{buttonText}</button>
                </form>
            </div>
        );
    };
});

jest.mock("../preloader/Preloader", () => {
    return function MockPreloader() {
        return <div>Preloader</div>;
    };
});

describe("components/addAd/AddAd", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    test("should render add ad form on /newAd", () => {
        render(
            <MemoryRouter initialEntries={["/newAd"]}>
                <AddAd id={1} handleAddAd={jest.fn()} isLoading={false} />
            </MemoryRouter>
        );

        expect(screen.getByText("Добавить новый товар")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "Добавить" })).toBeInTheDocument();
    });

    test("should show preloader when loading", () => {
        render(
            <MemoryRouter initialEntries={["/newAd"]}>
                <AddAd id={1} handleAddAd={jest.fn()} isLoading={true} />
            </MemoryRouter>
        );

        expect(screen.getByText("Preloader")).toBeInTheDocument();
    });

    test("should validate empty submit", async () => {
        render(
            <MemoryRouter initialEntries={["/newAd"]}>
                <AddAd id={1} handleAddAd={jest.fn()} isLoading={false} />
            </MemoryRouter>
        );

        fireEvent.click(screen.getByRole("button", { name: "Добавить" }));

        expect(await screen.findByText("Загрузите фотографию")).toBeInTheDocument();
        expect(screen.getAllByText("Это поле не должно быть пустым").length).toBeGreaterThan(0);
    });

    test("should validate short title and description", async () => {
        render(
            <MemoryRouter initialEntries={["/newAd"]}>
                <AddAd id={1} handleAddAd={jest.fn()} isLoading={false} />
            </MemoryRouter>
        );

        const textboxes = screen.getAllByRole("textbox");
        fireEvent.change(textboxes[0], { target: { value: "short" } });
        fireEvent.change(textboxes[1], { target: { value: "short" } });

        expect(await screen.findAllByText("Минимальное количество символов - 8"))
            .toHaveLength(2);
    });

    test("should validate price", async () => {
        render(
            <MemoryRouter initialEntries={["/newAd"]}>
                <AddAd id={1} handleAddAd={jest.fn()} isLoading={false} />
            </MemoryRouter>
        );

        fireEvent.change(screen.getByRole("spinbutton"), {
            target: { value: "0" },
        });

        expect(await screen.findByText("Цена должна быть больше 0")).toBeInTheDocument();
    });

    test("should validate file type", async () => {
        render(
            <MemoryRouter initialEntries={["/newAd"]}>
                <AddAd id={1} handleAddAd={jest.fn()} isLoading={false} />
            </MemoryRouter>
        );

        const file = new File(["text"], "doc.txt", { type: "text/plain" });
        const fileInput = document.querySelector('input[type="file"]');

        fireEvent.change(fileInput, {
            target: { files: [file] },
        });

        expect(await screen.findByText("Можно загружать только изображения")).toBeInTheDocument();
    });

    test("should submit valid ad and navigate to profile", async () => {
        const handleAddAd = jest.fn().mockResolvedValue({ pk: 1 });

        render(
            <MemoryRouter initialEntries={["/newAd"]}>
                <AddAd id={1} handleAddAd={handleAddAd} isLoading={false} />
            </MemoryRouter>
        );

        const textboxes = screen.getAllByRole("textbox");
        const priceInput = screen.getByRole("spinbutton");
        const fileInput = document.querySelector('input[type="file"]');

        const file = new File(["photo"], "photo.jpg", { type: "image/jpeg" });

        fireEvent.change(textboxes[0], { target: { value: "Valid title" } });
        fireEvent.change(fileInput, { target: { files: [file] } });
        fireEvent.change(priceInput, { target: { value: "1500" } });
        fireEvent.change(textboxes[1], { target: { value: "Valid description" } });

        fireEvent.click(screen.getByRole("button", { name: "Добавить" }));

        await waitFor(() => {
            expect(handleAddAd).toHaveBeenCalledWith({
                image: file,
                title: "Valid title",
                price: 1500,
                description: "Valid description",
            });
        });

        await waitFor(() => {
            expect(mockedNavigate).toHaveBeenCalledWith("/profile");
        });
    });
});