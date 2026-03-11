<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\Auth\LoginRequest;
use App\Http\Resources\UserResource;
use App\Services\AuthService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Validation\ValidationException;

class AuthController extends Controller
{
    protected $authService;

    public function __construct(AuthService $authService)
    {
        $this->authService = $authService;
    }

    /**
     * Handle user login and token generation.
     *
     * @param LoginRequest $request
     * @return JsonResponse
     */
    public function login(LoginRequest $request): JsonResponse
    {
        try {
            $result = $this->authService->authenticate($request->validated());

            return $this->successResponse(
                [
                    'user' => new UserResource($result['user']),
                    'token' => $result['token'],
                ],
                'User logged in successfully.'
            );
        } catch (ValidationException $e) {
            return $this->errorResponse($e->getMessage(), 401, $e->errors());
        }
    }

    /**
     * Handle user logout and token revocation.
     *
     * @param Request $request
     * @return JsonResponse
     */
    public function logout(Request $request): JsonResponse
    {
        $request->user()->currentAccessToken()->delete();

        return $this->successResponse(null, 'User logged out successfully.');
    }

    /**
     * Get the authenticated user's profile.
     *
     * @param Request $request
     * @return JsonResponse
     */
    public function user(Request $request): JsonResponse
    {
        return $this->successResponse(new UserResource($request->user()), 'User profile retrieved successfully.');
    }
}
