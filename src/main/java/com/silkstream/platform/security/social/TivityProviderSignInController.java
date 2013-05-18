package com.silkstream.platform.security.social;

import com.silkstream.platform.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.connect.web.ConnectSupport;
import org.springframework.social.connect.web.ProviderSignInAttempt;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/signin")
public class TivityProviderSignInController {

	private final static Log logger = LogFactory.getLog(TivityProviderSignInController.class);
	private final ConnectionFactoryLocator connectionFactoryLocator;
	private final UsersConnectionRepository usersConnectionRepository;
	private final SignInAdapter signInAdapter;
	private String signInUrl = "/signin";
	private String signUpUrl = "/signup";
	private String postSignInUrl = "/last";

	@Inject
	private UserService userService;

	private final ConnectSupport webSupport = new ConnectSupport();

	@Inject
	public TivityProviderSignInController(ConnectionFactoryLocator connectionFactoryLocator,
	                                      UsersConnectionRepository usersConnectionRepository,
	                                      SignInAdapter signInAdapter) {
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.usersConnectionRepository = usersConnectionRepository;
		this.signInAdapter = signInAdapter;
		this.webSupport.setUseAuthenticateUrl(true);
	}

	public void setSignInUrl(String signInUrl) {
		this.signInUrl = signInUrl;
	}
	
	public void setSignUpUrl(String signUpUrl) {
		this.signUpUrl = signUpUrl; 
	}

	public void setPostSignInUrl(String postSignInUrl) {
		this.postSignInUrl = postSignInUrl;
	}

	public void setApplicationUrl(String applicationUrl) {
		webSupport.setApplicationUrl(applicationUrl);
	}

	@RequestMapping(value="/{providerId}", method=RequestMethod.POST)
	public RedirectView signIn(@PathVariable("providerId") String providerId, NativeWebRequest request) {
		ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(providerId);
		try {
			return new RedirectView(webSupport.buildOAuthUrl(connectionFactory, request));
		} catch (Exception e) {
			return redirect(URIBuilder.fromUri(signInUrl).queryParam("error", "provider").build().toString());
		}
	}

	@RequestMapping(value="/{providerId}", method=RequestMethod.GET, params="oauth_token")
	public RedirectView oauth1Callback(@PathVariable("providerId") String providerId, NativeWebRequest request) {
		try {
			OAuth1ConnectionFactory<?> connectionFactory = (OAuth1ConnectionFactory<?>) connectionFactoryLocator.getConnectionFactory(providerId);
			Connection<?> connection = webSupport.completeConnection(connectionFactory, request);
			return handleSignIn(connection, request);
		} catch (Exception e) {
			return redirect(URIBuilder.fromUri(signInUrl).queryParam("error", "provider").build().toString());
		}
	}

	@RequestMapping(value="/{providerId}", method=RequestMethod.GET, params="code")
	public RedirectView oauth2Callback(@PathVariable("providerId") String providerId, @RequestParam("code") String code, NativeWebRequest request) {
		try {
			OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) connectionFactoryLocator.getConnectionFactory(providerId);
			Connection<?> connection = webSupport.completeConnection(connectionFactory, request);
			return handleSignIn(connection, request);
		} catch (Exception e) {
			logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + "). Redirecting to " + signInUrl);
			return redirect(URIBuilder.fromUri(signInUrl).queryParam("error", "provider").build().toString());
		}
	}
	
	@RequestMapping(value="/{providerId}", method=RequestMethod.GET)
	public RedirectView canceledAuthorizationCallback() {
		return redirect(signInUrl);
	}

	private RedirectView handleSignIn(Connection<?> connection, NativeWebRequest request) {
		List<String> userIds = usersConnectionRepository.findUserIdsWithConnection(connection);
		userService.rememberUser((HttpServletRequest) request.getNativeRequest(), (HttpServletResponse) request.getNativeResponse());
		if (userIds.size() == 0) {
			ProviderSignInAttempt signInAttempt = new ProviderSignInAttempt(connection, connectionFactoryLocator, usersConnectionRepository);
			request.setAttribute(ProviderSignInAttempt.class.getName(), signInAttempt, RequestAttributes.SCOPE_SESSION);
			return redirect(signUpUrl);
		} else if (userIds.size() == 1) {
			usersConnectionRepository.createConnectionRepository(userIds.get(0)).updateConnection(connection);
			String originalUrl = signInAdapter.signIn(userIds.get(0), connection, request);
			return originalUrl != null ? redirect(originalUrl) : redirect(postSignInUrl);
		} else {
			return redirect(URIBuilder.fromUri(signInUrl).queryParam("error", "multiple_users").build().toString());
		}
	}

	private RedirectView redirect(String url) {
		return new RedirectView(url, true);
	}

	private static final String PROVIDER_ERROR_ATTRIBUTE = "social.provider.error";
}