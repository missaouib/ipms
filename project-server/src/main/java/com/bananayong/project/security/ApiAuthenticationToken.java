package com.bananayong.project.security;

import java.util.Collection;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.CLASS;

@JsonTypeInfo(use = CLASS, property = "@class")
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, isGetterVisibility = NONE)
public class ApiAuthenticationToken extends AbstractAuthenticationToken { // NOSONAR

    private static final long serialVersionUID = -2137201805588460991L;

    private final String principal;
    private long id;
    private String credentials;
    private final Collection<String> roles;

    public ApiAuthenticationToken(@NotNull String principal, @NotNull String credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.roles = null;
        setAuthenticated(false);
    }

    @SuppressWarnings("WeakerAccess")
    @JsonCreator
    public ApiAuthenticationToken(
        @JsonProperty("id") long id,
        @NotNull @JsonProperty("principal") String principal,
        @SuppressWarnings("PMD.ArrayIsStoredDirectly")
        @NotNull @JsonProperty("roles") String... roles
    ) {
        super(AuthorityUtils.createAuthorityList(roles));
        this.principal = principal;
        this.credentials = null;
        this.roles = Set.of(roles);
        this.id = id;
        setAuthenticated(true);
        setDetails(id);
    }

    @JsonIgnore
    @Override
    public String getCredentials() {
        return this.credentials;
    }

    @Override
    public String getPrincipal() {
        return this.principal;
    }

    @JsonIgnore
    @Override
    public String getName() {
        return super.getName();
    }

    @JsonIgnore
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @JsonIgnore
    @Override
    public boolean isAuthenticated() {
        return super.isAuthenticated();
    }

    @JsonGetter
    @SuppressWarnings({"unused"})
    private String[] getRoles() {
        return this.roles.toArray(String[]::new);
    }

    @JsonIgnore
    @Override
    public Object getDetails() {
        return super.getDetails();
    }

    @SuppressWarnings("unused")
    public Long getId() {
        return this.id;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
