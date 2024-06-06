DESCRIPTION = "MLIR"
HOMEPAGE = "http://libclc.llvm.org/"
SECTION = "libs"

require clang.inc
require common-source.inc

TOOLCHAIN = "clang"

LIC_FILES_CHKSUM = "file://mlir/LICENSE.TXT;md5=7ef1911355f6e321fa0d7971689b7a67"

inherit cmake pkgconfig python3native qemu

DEPENDS += "qemu-native clang"

OECMAKE_SOURCEPATH = "${S}/mlir"

EXTRA_OECMAKE += " \
				-DCMAKE_CROSSCOMPILING_EMULATOR=${WORKDIR}/qemuwrapper \
                                -DMLIR_STANDALONE_BUILD=TRUE \
                                -DMLIR_INSTALL_AGGREGATE_OBJECTS=OFF \
			"

do_configure:prepend () {
	# Write out a qemu wrapper that will be used by cmake
	# so that it can run target helper binaries through that.
	qemu_binary="${@qemu_wrapper_cmdline(d, d.getVar('STAGING_DIR_HOST'), [d.expand('${STAGING_DIR_HOST}${libdir}'),d.expand('${STAGING_DIR_HOST}${base_libdir}')])}"
	cat > ${WORKDIR}/qemuwrapper << EOF
#!/bin/sh
$qemu_binary "\$@"
EOF
	chmod +x ${WORKDIR}/qemuwrapper
}

#FILES:${PN} += "${datadir}/clc"

BBCLASSEXTEND = "native nativesdk"

export YOCTO_ALTERNATE_EXE_PATH
export YOCTO_ALTERNATE_LIBDIR
