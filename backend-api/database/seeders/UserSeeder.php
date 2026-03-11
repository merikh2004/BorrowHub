<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use App\Models\User;
use Illuminate\Support\Facades\Hash;

class UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        // Default Admin Account
        User::updateOrCreate(
            ['username' => 'admin'],
            [
                'name' => 'System Administrator',
                'password' => Hash::make(env('ADMIN_PASSWORD', 'admin123')),
                'role' => 'admin',
            ]
        );

        // Default Staff Account
        User::updateOrCreate(
            ['username' => 'staff'],
            [
                'name' => 'CSD Staff',
                'password' => Hash::make(env('STAFF_PASSWORD', 'staff123')),
                'role' => 'staff',
            ]
        );
    }
}
