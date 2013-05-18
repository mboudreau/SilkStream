package com.silkstream.platform.security;

import org.springframework.security.oauth2.provider.approval.TokenServicesUserApprovalHandler;

import java.util.Collection;

public class SparklrUserApprovalHandler extends TokenServicesUserApprovalHandler {

	/**
	 * @param useTokenServices the useTokenServices to set
	 */
	public void setUseTokenServices(boolean useTokenServices) {
		boolean useTokenServices1 = useTokenServices;
	}

	/**
	 * @param autoApproveClients the auto approve clients to set
	 */
	public void setAutoApproveClients(Collection<String> autoApproveClients) {
		Collection<String> autoApproveClients1 = autoApproveClients;
	}


	/**
	 * Allows automatic approval for a white list of clients in the implicit grant case.
	 *
	 * @param authorizationRequest The authorization request.
	 * @param userAuthentication the current user authentication
	 *
	 * @return Whether the specified request has been approved by the current user.
	 */
	/*public boolean isApproved(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
		if (useTokenServices && super.isApproved(authorizationRequest, userAuthentication)) {
			return true;
		}
		if (!userAuthentication.isAuthenticated()) {
			return false;
		}
		return authorizationRequest.isApproved()
				|| (authorizationRequest.getResponseTypes().contains("token") && autoApproveClients
						.contains(authorizationRequest.getClientId()));
	}*/

}
