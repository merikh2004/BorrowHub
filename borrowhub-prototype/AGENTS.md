# Project Overview

This directory contains the codebase for the **BorrowHub Mobile App Wireframe**. It is a frontend web application built using **React**, **Vite**, **Tailwind CSS**, and various **Radix UI** components. The project serves as a functional prototype or wireframe for the BorrowHub mobile application.

# Building and Running

To work with this project, ensure you have Node.js installed. You can use standard `npm` commands:

- **Install dependencies:**
  ```sh
  npm i
  ```
- **Start the development server:**
  ```sh
  npm run dev
  ```
- **Build for production:**
  ```sh
  npm run build
  ```

# Development Conventions

- **Application Structure:**
  - **Screens/Pages:** Application views such as Login, Dashboard, and Inventory are located in `src/app/screens/`.
  - **UI Components:** Reusable UI components (many built on top of Radix UI) are kept in `src/app/components/ui/`.
- **Routing:** Navigation and route configuration are handled by `react-router` in `src/app/routes.tsx`.
- **Styling:** The project utilizes **Tailwind CSS** for styling. Core styles and themes are defined in the `src/styles/` directory.
- **Path Aliasing:** The `@/` alias is set up in `vite.config.ts` to map directly to the `src/` directory, simplifying imports.
- **Feature Documentation:** Always refer to `docs/features.md` for the authoritative list of all modules and functional deliverables in this prototype.