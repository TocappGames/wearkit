PWD := $(PWD)

version:
	docker run --rm -it -v $(PWD):/app detouched/standard-version
