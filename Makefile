.PHONY: lint test

test:
	lein test

lint:
	clj-kondo --parallel --lint src test
