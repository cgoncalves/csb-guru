INSERT INTO consumers (`key`, secret, display_name, connect_uri) VALUES ('9cnkiWf4XmzNVQZTEem1rJv0odSMYYikNQBuCzqsHWnMget3txlnfKpQw1QlEOxat7zbHPJoDXZHJCMFTUoRxcnyQ0YrazzL9ZwWAmezFMRkSVQ7JwRAovaa', 'KBUxy8vlBCej6VxpcJOb4PlmDN4U94O5cEYUJcpm6kqdhMiaiWMHFzu3wHvixE2jnRhoyfkr7mDBiORTn2GuCtF3YX38SN7AiQTKaUxcEwQ5kVrmSBtCdMuL', 'CSB OAuth Portal', 'http://localhost:3000/auth/csb/callback');
INSERT INTO users (name, email, password) VALUES ('cgoncalves', 'cgoncalves@av.it.pt', 'daaad6e5604e8e17bd9f108d91e26afe6281dac8fda0091040a7a6d7bd9b43b5');
INSERT INTO users (name, email, password) VALUES ('portal', 'mail@cgoncalves.pt', 'd29865e75555246582ddae7a45c872ed41781b50a0b4ed6d6940c581e671d711');
INSERT INTO roles (name) VALUES ('user');
INSERT INTO roles (name) VALUES ('admin');
INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO access_tokens (secret, token, consumer_key, user_id) VALUES ('9cnkiWf4XmzNVQZTEem1rJv0odSMYYikNQBuCzqsHWnMget3txlnfKpQw1QlEOxat7zbHPJoDXZHJCMFTUoRxcnyQ0YrazzL9ZwWAmezFMRkSVQ7JwRAovab', 'KBUxy8vlBCej6VxpcJOb4PlmDN4U94O5cEYUJcpm6kqdhMiaiWMHFzu3wHvixE2jnRhoyfkr7mDBiORTn2GuCtF3YX38SN7AiQTKaUxcEwQ5kVrmSBtCdMuM', '9cnkiWf4XmzNVQZTEem1rJv0odSMYYikNQBuCzqsHWnMget3txlnfKpQw1QlEOxat7zbHPJoDXZHJCMFTUoRxcnyQ0YrazzL9ZwWAmezFMRkSVQ7JwRAovaa', 2);


INSERT INTO paas_providers(DTYPE, id, state, domain) VALUES ('PrivatePaas', 'PT_AMAZON', 0, 'api.aws.cgoncalves.pt');
INSERT INTO paas_providers(DTYPE, id, state, domain) VALUES ('PrivatePaas', 'PT_RACKSPACE', 0, 'api.rs.cgoncalves.pt');
INSERT INTO paas_providers(DTYPE, id, state, domain) VALUES ('PrivatePaas', 'PT_OPENSTACK', 0, 'api.cf.cgoncalves.pt');