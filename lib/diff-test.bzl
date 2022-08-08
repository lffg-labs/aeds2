"""Provides diff testing functionality."""

load("@bazel_skylib//rules:diff_test.bzl", skylib_diff_test = "diff_test")

def diff_test(name, bin, input, output):
    actual_output = "{}_actual-output".format(name)

    native.genrule(
        name = actual_output,
        srcs = [input],
        outs = ["{}.txt".format(actual_output)],
        cmd = "cat $< | $(location {}) > $@".format(bin),
        tools = [bin],
    )

    skylib_diff_test(
        name = name,
        file1 = ":{}".format(actual_output),
        file2 = output,
    )
