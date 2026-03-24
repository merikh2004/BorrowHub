<?php

namespace Tests\Feature;

use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Illuminate\Support\Facades\Hash;
use Tests\TestCase;

class AccountSettingsTest extends TestCase
{
    use RefreshDatabase;

    protected $user;

    protected function setUp(): void
    {
        parent::setUp();
        
        $this->user = User::factory()->create([
            'name' => 'Original Name',
            'username' => 'originaluser',
            'password' => Hash::make('oldpassword')
        ]);
    }

    public function test_user_can_update_profile()
    {
        $response = $this->actingAs($this->user)
                         ->patchJson('/api/v1/user/profile', [
                             'name' => 'Updated Name',
                             'username' => 'updateduser'
                         ]);

        $response->assertStatus(200)
                 ->assertJsonPath('data.name', 'Updated Name')
                 ->assertJsonPath('data.username', 'updateduser');

        $this->assertDatabaseHas('users', [
            'id' => $this->user->id,
            'name' => 'Updated Name',
            'username' => 'updateduser'
        ]);
    }

    public function test_user_cannot_update_profile_with_duplicate_username()
    {
        User::factory()->create(['username' => 'takenuser']);

        $response = $this->actingAs($this->user)
                         ->patchJson('/api/v1/user/profile', [
                             'name' => 'Updated Name',
                             'username' => 'takenuser'
                         ]);

        $response->assertStatus(422)
                 ->assertJsonValidationErrors('username');
    }

    public function test_user_can_update_password()
    {
        $response = $this->actingAs($this->user)
                         ->patchJson('/api/v1/user/password', [
                             'current_password' => 'oldpassword',
                             'new_password' => 'newpassword123',
                             'new_password_confirmation' => 'newpassword123'
                         ]);

        $response->assertStatus(200);

        $this->user->refresh();
        $this->assertTrue(Hash::check('newpassword123', $this->user->password));
    }

    public function test_user_cannot_update_password_with_incorrect_current_password()
    {
        $response = $this->actingAs($this->user)
                         ->patchJson('/api/v1/user/password', [
                             'current_password' => 'wrongpassword',
                             'new_password' => 'newpassword123',
                             'new_password_confirmation' => 'newpassword123'
                         ]);

        $response->assertStatus(422)
                 ->assertJsonValidationErrors('current_password');
    }

    public function test_user_cannot_update_password_with_mismatched_confirmation()
    {
        $response = $this->actingAs($this->user)
                         ->patchJson('/api/v1/user/password', [
                             'current_password' => 'oldpassword',
                             'new_password' => 'newpassword123',
                             'new_password_confirmation' => 'mismatch'
                         ]);

        $response->assertStatus(422)
                 ->assertJsonValidationErrors('new_password');
    }

    public function test_user_cannot_update_password_with_weak_new_password()
    {
        $response = $this->actingAs($this->user)
                         ->patchJson('/api/v1/user/password', [
                             'current_password' => 'oldpassword',
                             'new_password' => 'weak',
                             'new_password_confirmation' => 'weak'
                         ]);

        $response->assertStatus(422)
                 ->assertJsonValidationErrors('new_password');
    }
}
