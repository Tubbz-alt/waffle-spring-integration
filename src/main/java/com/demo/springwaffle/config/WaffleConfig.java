package com.demo.springwaffle.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.demo.springwaffle.filter.UserContextInjectionFilter;

import waffle.servlet.spi.BasicSecurityFilterProvider;
import waffle.servlet.spi.NegotiateSecurityFilterProvider;
import waffle.servlet.spi.SecurityFilterProvider;
import waffle.servlet.spi.SecurityFilterProviderCollection;
import waffle.spring.NegotiateSecurityFilter;
import waffle.spring.NegotiateSecurityFilterEntryPoint;
import waffle.spring.WindowsAuthenticationProvider;
import waffle.windows.auth.impl.WindowsAuthProviderImpl;

@Configuration
public class WaffleConfig {
	@Bean
	public WindowsAuthProviderImpl waffleWindowsAuthProvider() {
		return new WindowsAuthProviderImpl();
	}

	@Bean
	public WindowsAuthenticationProvider waffleSpringAuthenticationProvider(
			final WindowsAuthProviderImpl waffleWindowsAuthProvider) {
		final WindowsAuthenticationProvider bean = new WindowsAuthenticationProvider();
		bean.setAuthProvider(waffleWindowsAuthProvider);
		bean.setPrincipalFormat("fqn");
		bean.setRoleFormat("both");
		bean.setAllowGuestLogin(false);
		return bean;
	}

	@Bean
	public NegotiateSecurityFilterProvider negotiateSecurityFilterProvider(
			final WindowsAuthProviderImpl windowsAuthProvider) {
		final NegotiateSecurityFilterProvider bean = new NegotiateSecurityFilterProvider(windowsAuthProvider);
		bean.setProtocols(Arrays.asList("Negotiate", "NTLM"));
		return bean;
	}

	@Bean
	public BasicSecurityFilterProvider basicSecurityFilterProvider(final WindowsAuthProviderImpl windowsAuthProvider) {
		return new BasicSecurityFilterProvider(windowsAuthProvider);
	}

	@Bean
	public SecurityFilterProviderCollection waffleSecurityFilterProviderCollection(
			final NegotiateSecurityFilterProvider negotiateProvider, final BasicSecurityFilterProvider basicProvider) {
		SecurityFilterProvider[] providers;
		providers = new SecurityFilterProvider[] { negotiateProvider, basicProvider };
		return new SecurityFilterProviderCollection(providers);
	}

	@Bean
	public NegotiateSecurityFilterEntryPoint negotiateSecurityFilterEntryPoint(
			final SecurityFilterProviderCollection providers) {
		final NegotiateSecurityFilterEntryPoint bean = new NegotiateSecurityFilterEntryPoint();
		bean.setProvider(providers);
		return bean;
	}

	@Bean
	public NegotiateSecurityFilter waffleNegotiateSecurityFilter(final SecurityFilterProviderCollection providers) {
		final NegotiateSecurityFilter bean = new NegotiateSecurityFilter();
		bean.setProvider(providers);
		bean.setPrincipalFormat("fqn");
		bean.setRoleFormat("both");
		bean.setAllowGuestLogin(false);
		bean.setImpersonate(false);
		return bean;
	}

	@Bean
	public FilterRegistrationBean<NegotiateSecurityFilter> waffleNegotiateSecurityFilterRegistrationBean(
			final NegotiateSecurityFilter filter) {
		final FilterRegistrationBean<NegotiateSecurityFilter> bean = new FilterRegistrationBean<>(filter);
		bean.setEnabled(false);
		return bean;
	}

	@Bean
	public FilterRegistrationBean<UserContextInjectionFilter> userContextInjectionFilterRegistrationBean(
			final UserContextInjectionFilter filter) {
		final FilterRegistrationBean<UserContextInjectionFilter> bean = new FilterRegistrationBean<>(filter);
		bean.setEnabled(false);
		return bean;
	}

}
